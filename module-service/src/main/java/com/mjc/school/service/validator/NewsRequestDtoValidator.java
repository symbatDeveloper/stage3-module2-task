package com.mjc.school.service.validator;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.model.Author;
import com.mjc.school.repository.model.NewsModel;
import com.mjc.school.service.dto.ServiceNewsRequestDto;
import com.mjc.school.service.exceptions.NotFoundException;
import com.mjc.school.service.exceptions.ValidatorException;

import static com.mjc.school.service.exceptions.ExceptionsCodes.*;

public class NewsRequestDtoValidator {

    private final BaseRepository<Author, Long> authorRepository;
    private final BaseRepository<NewsModel, Long> newsRepository;

    public NewsRequestDtoValidator(BaseRepository<Author, Long> authorRepository,
                                   BaseRepository<NewsModel, Long> newsRepository) {
        this.authorRepository = authorRepository;
        this.newsRepository = newsRepository;
    }


    public void validateNewsDTORequest(ServiceNewsRequestDto dto) {
        checkNewsTitle(dto.getTitle());
        checkNewsContent(dto.getContent());
    }

    public void validateNewsId(Long id) {
        if (id == null || id < 1) {
            throw new ValidatorException(
                    String.format(VALIDATE_NEGATIVE_OR_NULL_NUMBER.getMessage(),
                            "News id", "News id", id));
        }
        if (!newsRepository.existById(id)) {
            throw new NotFoundException(
                    String.format(NEWS_ID_DOES_NOT_EXIST.getMessage(), id));
        }
    }

    public void validateAuthorId(Long id) {
        if (id == null)
            return;
        if (id < 1) {
            throw new ValidatorException(
                    String.format(VALIDATE_NEGATIVE_OR_NULL_NUMBER.getMessage(), "Author id", "Author id", id));
        }
        if (!authorRepository.existById(id)) {
            throw new NotFoundException(String.format(AUTHOR_ID_DOES_NOT_EXIST.getMessage(), id));
        }
    }

    private void checkNewsTitle(String title) {
        if (title == null || title.length() < 5 || title.length() > 30) {
            throw new ValidatorException(
                    String.format(VALIDATE_STRING_LENGTH.getMessage(),
                            "News title", 5, 30, "News title", title)
            );
        }
    }

    private void checkNewsContent(String content) {
        if (content == null || content.length() < 5 || content.length() > 255) {
            throw new ValidatorException(
                    String.format(VALIDATE_STRING_LENGTH.getMessage(),
                            "News content", 5, 255, "News content", content)
            );
        }
    }
}
