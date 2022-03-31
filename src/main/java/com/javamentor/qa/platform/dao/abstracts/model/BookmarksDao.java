package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.BookMarks;

import java.util.List;

public interface BookmarksDao extends ReadWriteDao<BookMarks, Long> {

    List<BookMarks> getBookmarksByUserAndQuestion(Long userId, Long questionId);

}
