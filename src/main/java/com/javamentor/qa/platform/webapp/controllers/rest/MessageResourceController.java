package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.dao.impl.pagination.messagedto.MessagePageDtoDaoAllMessagesInGlobalChat;
import com.javamentor.qa.platform.dao.impl.pagination.messagedto.MessagePageDtoDaoFindMessagesInGlobalChat;
import com.javamentor.qa.platform.models.dto.MessageDto;
import com.javamentor.qa.platform.models.dto.PageDTO;
import com.javamentor.qa.platform.models.entity.chat.Message;
import com.javamentor.qa.platform.models.entity.chat.MessageStar;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.MessageDtoService;
import com.javamentor.qa.platform.service.abstracts.model.MessageService;
import com.javamentor.qa.platform.service.abstracts.model.MessageStarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import java.util.Optional;

@Tag(name = "MessageResourceController", description = "Позволяет работать с избранными сообщениями")
@RestController
@RequestMapping("/api/user/message")
public class MessageResourceController {
    private final MessageStarService messageStarService;
    private final MessageService messageService;
    private final MessageDtoService messageDtoService;

    @Autowired
    public MessageResourceController(MessageStarService messageStarService, MessageService messageService, MessageDtoService messageDtoService) {
        this.messageStarService = messageStarService;
        this.messageService = messageService;
        this.messageDtoService = messageDtoService;
    }

    @Operation(summary = "Получение списка всех сообщений в глобальном чате.",
            description = "Получение пагинированного списка сообщений в глобальном чате.")
    @ApiResponse(responseCode = "200", description = "Сообщения выведены в глобальный чат", content = {
            @Content(mediaType = "application/json")
    })
    @ApiResponse(responseCode = "400", description = "Сообщения не выведены в глобальный чат", content = {
            @Content(mediaType = "application/json")
    })
    @GetMapping("/global")
    public ResponseEntity<PageDTO<MessageDto>> getAllMessageInGlobalChat(@RequestParam(defaultValue = "1") int currentPage,
                                                                         @RequestParam(defaultValue = "10") int items) {
        PaginationData data = new PaginationData(
                currentPage,
                items,
                MessagePageDtoDaoAllMessagesInGlobalChat.class.getSimpleName());
        return new ResponseEntity<>(messageDtoService.getPageDto(data), HttpStatus.OK);
    }

    @Operation(summary = "Получение пагинированного списка сообщений с неточным поиском в глобальном чате.",
            description = "Получение пагинированного списка сообщений с неточным поиском в глобальном чате.")
    @ApiResponse(responseCode = "200", description = "Найденные сообщения выведены в глобальный чат", content = {
            @Content(mediaType = "application/json")
    })
    @ApiResponse(responseCode = "400", description = "Найденные Сообщения не выведены в глобальный чат", content = {
            @Content(mediaType = "application/json")
    })
    @GetMapping("/global/find")
    public ResponseEntity<PageDTO<MessageDto>> findMessagesInGlobalChat(@RequestParam(defaultValue = "") String text,
                                                                        @RequestParam(defaultValue = "1") int currentPage,
                                                                        @RequestParam(defaultValue = "10") int items) {
        PaginationData data = new PaginationData(
                currentPage,
                items,
                MessagePageDtoDaoFindMessagesInGlobalChat.class.getSimpleName());
        data.getProps().put("text", text);
        return new ResponseEntity<>(messageDtoService.getPageDto(data), HttpStatus.OK);
    }

    @Operation(summary = "Сохранение сообщения в избранное аутентифицированного юзера",
            description = "Принимает ID сообщения, которое нужно добавить в избранное")
    @ApiResponse(responseCode = "200", description = "Удалось сохранить сообщение в избранное", content = {
            @Content(mediaType = "application/json")
    })
    @ApiResponse(responseCode = "400", description = "Не удалось сохранить, пришло некорректное ID", content = {
            @Content(mediaType = "application/json")
    })
    @PostMapping("/star")
    public ResponseEntity<?> addMessageToStarMessages(@RequestBody Long messageId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Message> messageToStar = messageService.getById(messageId);
        Optional<MessageStar> messageStar = messageStarService.getMessageStarByUserAndMessage(user.getId(), messageId);
        if (messageStar.isPresent()) {
            return new ResponseEntity<>("Сообщение с ID " + messageId +
                    " уже есть в избранном у пользователя с ID " + user.getId(), HttpStatus.BAD_REQUEST);
        }
        if (messageToStar.isPresent()) {
            MessageStar message = new MessageStar();
            message.setUser(user);
            message.setMessage(messageToStar.get());
            messageStarService.persist(message);
            return new ResponseEntity<>("Message with id = " + messageId + " was successfully add to stars", HttpStatus.OK);
        }
        return new ResponseEntity<>("Пришло некорректное ID(" + messageId + ") сообщения для сохранения", HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Удаление сообщения из избранных у авторизованного пользователя",
            description = "Принимает ID сообщения, которое нужно удалить из избранных")
    @ApiResponse(responseCode = "200", description = "Сообщение успешно удалено из избранных", content = {
            @Content(mediaType = "application/json")
    })
    @ApiResponse(responseCode = "400", description = "При удалении сообщения из избранных произошла ошибка:" +
            " сообщение с таким ID не существует в избранных", content = {
            @Content(mediaType = "application/json")
    })
    @DeleteMapping("/star")
    public ResponseEntity<?> deleteMessageStarByMessageId(@RequestBody Long messageId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<MessageStar> messageStar = messageStarService.getMessageStarByUserAndMessage(user.getId(), messageId);
        if (messageStar.isPresent()) {
            messageStarService.deleteById(messageId);
            return new ResponseEntity<>("Сообщение с id = " + messageId + " было успешно удалено из избранных", HttpStatus.OK);
        }
        return new ResponseEntity<>("Сообщение с id = " + messageId + " не существует в избранных пользователя " +
                user.getNickname(), HttpStatus.BAD_REQUEST);
    }
}
