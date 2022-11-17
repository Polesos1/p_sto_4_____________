package com.javamentor.qa.platform.api;

import com.javamentor.qa.platform.AbstractClassForDRRiderMockMVCTests;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestChatResourceController extends AbstractClassForDRRiderMockMVCTests {

    //�������, ���� 3 ����, 2 �� ��� ���������, � ����� ��������� user100@mail.ru
    @Test
    @Sql("script/testChatResourceController/shouldGetAllSingleChats/Before.sql")
    @Sql(scripts = "script/testChatResourceController/shouldGetAllSingleChats/After.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldGetAllSingleChats() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/user/chat/single")
                        .contentType("application/json")
                        .header("Authorization",
                                "Bearer " + getToken("user100@mail.ru", "user100")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$.[0].id").value(100))
                .andExpect(jsonPath("$.[0].name").value("user101@mail.ru"))
                .andExpect(jsonPath("$.[0].image").value("/images/noUserAvatar.png"))
                .andExpect(jsonPath("$.[0].lastMessage").value("message_from_chat_100_and_user_100"))
                .andExpect(jsonPath("$.[0].persistDateTimeLastMessage").value("2022-10-03T00:00:00"))
                .andExpect(jsonPath("$.[1].id").value(102))
                .andExpect(jsonPath("$.[1].name").value("user102@mail.ru"))
                .andExpect(jsonPath("$.[1].image").value("/images/noUserAvatar.png"))
                .andExpect(jsonPath("$.[1].lastMessage").value("message_from_chat_102_and_user_102"))
                .andExpect(jsonPath("$.[1].persistDateTimeLastMessage").value("2022-10-01T00:00:00"));
    }

    //�������, ���� 3 ����, ��� �� ��� ���������
    @Test
    @Sql("script/testChatResourceController/shouldGetZeroSingleChatsBecauseAllChatsAreGroup/Before.sql")
    @Sql(scripts = "script/testChatResourceController/shouldGetZeroSingleChatsBecauseAllChatsAreGroup/After.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldGetZeroSingleChatsBecauseAllChatsAreGroup() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/user/chat/single")
                        .contentType("application/json")
                        .header("Authorization",
                                "Bearer " + getToken("user100@mail.ru", "user100")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*").isEmpty());
    }

    //�������, ����� ����������� �����
    @Test
    @Sql("script/testChatResourceController/shouldGet403StatusWhenUserIsAdmin/Before.sql")
    @Sql(scripts = "script/testChatResourceController/shouldGet403StatusWhenUserIsAdmin/After.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldGet403StatusWhenUserIsAdmin() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/user/chat/single")
                        .contentType("application/json")
                        .header("Authorization",
                                "Bearer " + getToken("user100@mail.ru", "user100")))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    //�������, ���� 3 ����, 2 �� ��� ���������, � ����� ��������� � ����������� user100@mail.ru � ����� User, ��������� Admin
    @Test
    @Sql("script/testChatResourceController/shouldGetAllSingleChatsWithAdmins/Before.sql")
    @Sql(scripts = "script/testChatResourceController/shouldGetAllSingleChatsWithAdmins/After.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldGetAllSingleChatsWithAdmins() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/user/chat/single")
                        .contentType("application/json")
                        .header("Authorization",
                                "Bearer " + getToken("user100@mail.ru", "user100")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$.[0].id").value(100))
                .andExpect(jsonPath("$.[0].name").value("user101@mail.ru"))
                .andExpect(jsonPath("$.[0].image").value("/images/noUserAvatar.png"))
                .andExpect(jsonPath("$.[0].lastMessage").value("message_from_chat_100_and_user_100"))
                .andExpect(jsonPath("$.[0].persistDateTimeLastMessage").value("2022-10-03T00:00:00"))
                .andExpect(jsonPath("$.[1].id").value(102))
                .andExpect(jsonPath("$.[1].name").value("user102@mail.ru"))
                .andExpect(jsonPath("$.[1].image").value("/images/noUserAvatar.png"))
                .andExpect(jsonPath("$.[1].lastMessage").value("message_from_chat_102_and_user_102"))
                .andExpect(jsonPath("$.[1].persistDateTimeLastMessage").value("2022-10-01T00:00:00"));
    }

    //�������, ���� 3 ����, 2 �� ��� ��������� � � ��� ��������� �������������� ������������, items = 10, currentPage = 1
    @Test
    @Sql("script/testChatResourceController/shouldGetAllGroupChats/Before.sql")
    @Sql(scripts = "script/testChatResourceController/shouldGetAllGroupChats/After.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldGetAllGroupChats() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/user/chat")
                        .contentType("application/json")
                        .header("Authorization",
                                "Bearer " + getToken("user100@mail.ru", "user100")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.size()").value(2))
                .andExpect(jsonPath("$.items[0].id").value(100))
                .andExpect(jsonPath("$.items[0].name").value("group_chat_100"))
                .andExpect(jsonPath("$.items[0].image").value("/images/noUserAvatar.png"))
                .andExpect(jsonPath("$.items[0].lastMessage").value("message_from_chat_100_and_user_100"))
                .andExpect(jsonPath("$.items[0].persistDateTimeLastMessage").value("2022-10-03T00:00:00"))
                .andExpect(jsonPath("$.items[1].id").value(102))
                .andExpect(jsonPath("$.items[1].name").value("group_chat_102"))
                .andExpect(jsonPath("$.items[1].image").value("/images/noUserAvatar.png"))
                .andExpect(jsonPath("$.items[1].lastMessage").value("message_from_chat_102_and_user_102"))
                .andExpect(jsonPath("$.items[1].persistDateTimeLastMessage").value("2022-10-01T00:00:00"));
    }

    //�������, ���� 3 ����, 2 �� ��� ��������� � � ��� ��������� �������������� ������������, items = 1, currentPage = 1
    @Test
    @Sql("script/testChatResourceController/shouldGetGroupChatWithVariables/Before.sql")
    @Sql(scripts = "script/testChatResourceController/shouldGetGroupChatWithVariables/After.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldGetGroupChatWithVariables() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/user/chat?currentPage=1&items=1")
                        .contentType("application/json")
                        .header("Authorization",
                                "Bearer " + getToken("user100@mail.ru", "user100")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.size()").value(1))
                .andExpect(jsonPath("$.items[0].id").value(100))
                .andExpect(jsonPath("$.items[0].name").value("group_chat_100"))
                .andExpect(jsonPath("$.items[0].image").value("/images/noUserAvatar.png"))
                .andExpect(jsonPath("$.items[0].lastMessage").value("message_from_chat_100_and_user_100"))
                .andExpect(jsonPath("$.items[0].persistDateTimeLastMessage").value("2022-10-03T00:00:00"));
    }

    //�������, ���� 3 ����, 2 �� ��� ��������� � � ��� �� ��������� �������������� ������������, items = 10, currentPage = 1
    @Test
    @Sql("script/testChatResourceController/shouldZeroGroupChatWhenThatChatsAreWithoutThisUser/Before.sql")
    @Sql(scripts = "script/testChatResourceController/shouldZeroGroupChatWhenThatChatsAreWithoutThisUser/After.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldZeroGroupChatWhenThatChatsAreWithoutThisUser() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/user/chat")
                        .contentType("application/json")
                        .header("Authorization",
                                "Bearer " + getToken("user100@mail.ru", "user100")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isEmpty());
    }

    //�������: �������� ��������� ����, ���� 4 ���� �� ��� � 3 ���� ���������������� ������������� � �� ���� 3, ��� group ���� � 1 single ���, items = 10, currentPage = 1
    @Test
    @Sql("script/testChatResourceController/shouldGetGroupChatsWhenAuthorizedUserInTwoGroupChatsAndOneSingleChat/Before.sql")
    @Sql(scripts = "script/testChatResourceController/shouldGetGroupChatsWhenAuthorizedUserInTwoGroupChatsAndOneSingleChat/After.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldGetGroupChatsWhenAuthorizedUserInTwoGroupChatsAndOneSingleChat() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/user/chat")
                        .contentType("application/json")
                        .header("Authorization",
                                "Bearer " + getToken("user100@mail.ru", "user100")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.size()").value(2))
                .andExpect(jsonPath("$.items[0].id").value(100))
                .andExpect(jsonPath("$.items[0].name").value("group_chat_100"))
                .andExpect(jsonPath("$.items[0].image").value("/images/noUserAvatar.png"))
                .andExpect(jsonPath("$.items[0].lastMessage").value("message_from_chat_100_and_user_100"))
                .andExpect(jsonPath("$.items[0].persistDateTimeLastMessage").value("2022-10-03T00:00:00"))
                .andExpect(jsonPath("$.items[1].id").value(102))
                .andExpect(jsonPath("$.items[1].name").value("group_chat_102"))
                .andExpect(jsonPath("$.items[1].image").value("/images/noUserAvatar.png"))
                .andExpect(jsonPath("$.items[1].lastMessage").value("message_from_chat_102_and_user_102"))
                .andExpect(jsonPath("$.items[1].persistDateTimeLastMessage").value("2022-10-01T00:00:00"));
    }

    //�������: �������� single ����, ���� 4 ���� �� ��� � 3 ���� ���������������� ������������� � �� ���� 3, ��� ����� ���� � 1 ����� ���, items = 10, currentPage = 1
    @Test
    @Sql("script/testChatResourceController/shouldGetSingleChatsWhenAuthorizedUserInTwoSingleChatsAndOneGroupChat/Before.sql")
    @Sql(scripts = "script/testChatResourceController/shouldGetSingleChatsWhenAuthorizedUserInTwoSingleChatsAndOneGroupChat/After.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldGetSingleChatsWhenAuthorizedUserInTwoSingleChatsAndOneGroupChat() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/user/chat/single")
                        .contentType("application/json")
                        .header("Authorization",
                                "Bearer " + getToken("user100@mail.ru", "user100")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$.[0].id").value(101))
                .andExpect(jsonPath("$.[0].name").value("user101@mail.ru"))
                .andExpect(jsonPath("$.[0].image").value("/images/noUserAvatar.png"))
                .andExpect(jsonPath("$.[0].lastMessage").value("message_from_chat_101_and_user_101"))
                .andExpect(jsonPath("$.[0].persistDateTimeLastMessage").value("2022-10-02T00:00:00"))
                .andExpect(jsonPath("$.[1].id").value(103))
                .andExpect(jsonPath("$.[1].name").value("user102@mail.ru"))
                .andExpect(jsonPath("$.[1].image").value("/images/noUserAvatar.png"))
                .andExpect(jsonPath("$.[1].lastMessage").value("message_from_chat_103_and_user_102"))
                .andExpect(jsonPath("$.[1].persistDateTimeLastMessage").value("2022-10-04T00:00:00"));
    }



    // ������������ �������� � ��������� ��� (��� - ����������, ��������� - ����� ����, ������������ - �� ������� � ����, �������� userId - ����������)
    @Test
    @Sql(scripts = "/script/TestChatResourceController/addUserInGroupChat_shouldAddUserInChat_whenExists/Before.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/script/TestChatResourceController/addUserInGroupChat_shouldAddUserInChat_whenExists/After.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addUserInGroupChat_shouldAddUserInChat_whenExists() throws Exception {

        mockMvc.perform(post("/api/user/chat/group/{id}/join", 101)
                        .param("userId", "102")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + getToken("user101@mail.ru", "user101"))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Is.is("userAdded")));
    }

    // ������������ �� �������� � ��������� ��� (��� - ����������, ��������� - ����� ����, ������������ - �� ������� � ����, �������� userId - �� ����������)
    @Test
    @Sql(scripts = "/script/TestChatResourceController/addUserInGroupChat_shouldBadRequest_whenParameterNotPass/Before.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/script/TestChatResourceController/addUserInGroupChat_shouldBadRequest_whenParameterNotPass/After.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addUserInGroupChat_shouldBadRequest_whenParameterNotPass() throws Exception {

        mockMvc.perform(post("/api/user/chat/group/{id}/join", 101)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + getToken("user101@mail.ru", "user101"))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    // ������������ �� �������� � ��������� ��� (��� - ����������, ��������� - ����� ����, ������������ - ������� � ����, �������� userId - ����������)
    @Test
    @Sql(scripts = "/script/TestChatResourceController/addUserInGroupChat_shouldBadRequest_whenUserPresent/Before.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/script/TestChatResourceController/addUserInGroupChat_shouldBadRequest_whenUserPresent/After.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addUserInGroupChat_shouldBadRequest_whenUserPresent() throws Exception {

        mockMvc.perform(post("/api/user/chat/group/{id}/join", 101)
                        .param("userId", "102")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + getToken("user101@mail.ru", "user101"))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Is.is("userPresent")));
    }

    // ������������ �� �������� � ��������� ��� (��� - ����������, ��������� - �� ����� ����, ������������ - �� ������� � ����, �������� userId - ����������)
    @Test
    @Sql(scripts = "/script/TestChatResourceController/addUserInGroupChat_shouldBadRequest_whenChatAuthorNotAdd/Before.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/script/TestChatResourceController/addUserInGroupChat_shouldBadRequest_whenChatAuthorNotAdd/After.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addUserInGroupChat_shouldBadRequest_whenChatAuthorNotAdd() throws Exception {

        mockMvc.perform(post("/api/user/chat/group/{id}/join", 101)
                        .param("userId", "103")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + getToken("user102@mail.ru", "user102"))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Is.is("This user with id 102 can't invite other users")));
    }

    // ������������ �� �������� � ��������� ��� (��� - ����������, ��������� - �� ����� ����, ������������ - ������� � ����, �������� userId - ����������)
    @Test
    @Sql(scripts = "/script/TestChatResourceController/addUserInGroupChat_shouldBadRequest_whenChatAuthorNotAddAndUserPresent/Before.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/script/TestChatResourceController/addUserInGroupChat_shouldBadRequest_whenChatAuthorNotAddAndUserPresent/After.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addUserInGroupChat_shouldBadRequest_whenChatAuthorNotAddAndUserPresent() throws Exception {

        mockMvc.perform(post("/api/user/chat/group/{id}/join", 101)
                        .param("userId", "103")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + getToken("user102@mail.ru", "user102"))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Is.is("This user with id 102 can't invite other users")));
    }

    // ������������ �� �������� � ��������� ��� (��� - �� ����������, ��������� - ����� ����, ������������ - �� ������� � ����, �������� userId - ����������)
    @Test
    @Sql(scripts = "/script/TestChatResourceController/addUserInGroupChat_shouldBadRequest_whenChatNotExists/Before.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/script/TestChatResourceController/addUserInGroupChat_shouldBadRequest_whenChatNotExists/After.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addUserInGroupChat_shouldBadRequest_whenChatNotExists() throws Exception {

        mockMvc.perform(post("/api/user/chat/group/{id}/join", 150)
                        .param("userId", "102")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + getToken("user101@mail.ru", "user101"))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Is.is("it's bad request")));
    }

    // ������������ �� �������� � ��������� ��� (��� - ����������, ��������� - ����� ����, ������������ - �� ����������, �������� userId - ����������)
    @Test
    @Sql(scripts = "/script/TestChatResourceController/addUserInGroupChat_shouldBadRequest_whenUserNotExists/Before.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/script/TestChatResourceController/addUserInGroupChat_shouldBadRequest_whenUserNotExists/After.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addUserInGroupChat_shouldBadRequest_whenUserNotExists() throws Exception {

        mockMvc.perform(post("/api/user/chat/group/{id}/join", 101)
                        .param("userId", "150")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + getToken("user101@mail.ru", "user101"))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Is.is("it's bad request")));
    }

    // ������������ �������� � ��������� ��� (��� - ����������, ��������� - ������������ � ����� �����(������� � ���� � �������� ������� ����), ������������ - �� ������� � ����, �������� userId - ����������)
    @Test
    @Sql(scripts = "/script/TestChatResourceController/addUserInGroupChat_shouldForbidden_whenChatAuthorAdmin/Before.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/script/TestChatResourceController/addUserInGroupChat_shouldForbidden_whenChatAuthorAdmin/After.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addUserInGroupChat_shouldForbidden_whenChatAuthorAdmin() throws Exception {

        mockMvc.perform(post("/api/user/chat/group/{id}/join", 101)
                        .param("userId", "122")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + getToken("user121@mail.ru", "user121"))
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    // ������������ �������� � ��������� ��� (��� - ����������, ��������� - ������������ � ����� �����(�� �������� ������� ����), ������������ - �� ������� � ����, �������� userId - ����������)
    @Test
    @Sql(scripts = "/script/TestChatResourceController/addUserInGroupChat_shouldForbidden_whenAdmin/Before.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/script/TestChatResourceController/addUserInGroupChat_shouldForbidden_whenAdmin/After.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addUserInGroupChat_shouldForbidden_whenAdmin() throws Exception {

        mockMvc.perform(post("/api/user/chat/group/{id}/join", 101)
                        .param("userId", "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + getToken("user121@mail.ru", "user121"))
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
