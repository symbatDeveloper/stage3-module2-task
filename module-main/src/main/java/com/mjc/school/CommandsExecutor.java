package com.mjc.school;

import com.mjc.school.commands.Command;
import com.mjc.school.commands.CommandType;
import com.mjc.school.commands.author.CreateAuthorCommand;
import com.mjc.school.commands.author.DeleteAuthorCommand;
import com.mjc.school.commands.author.ReadAllAuthorsCommand;
import com.mjc.school.commands.author.ReadAuthorByIdCommand;
import com.mjc.school.commands.author.UpdateAuthorCommand;
import com.mjc.school.commands.news.CreateNewsCommand;
import com.mjc.school.commands.news.DeleteNewsCommand;
import com.mjc.school.commands.news.ReadAllNewsCommand;
import com.mjc.school.commands.news.ReadNewsByIdCommand;
import com.mjc.school.commands.news.UpdateNewsCommand;
import com.mjc.school.controller.BaseController;
import com.mjc.school.controller.dto.AuthorRequestDto;
import com.mjc.school.controller.dto.AuthorResponseDto;
import com.mjc.school.controller.dto.NewsRequestDto;
import com.mjc.school.controller.dto.NewsResponseDto;
import com.mjc.school.exceptions.IdFormatException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class CommandsExecutor {

    private final BaseController<NewsRequestDto, NewsResponseDto, Long> newsController;
    private final BaseController<AuthorRequestDto, AuthorResponseDto, Long> authorController;

    private final TerminalCommandsReader commandsReader = new TerminalCommandsReader();

    public CommandsExecutor(@Qualifier("newsController")
                            BaseController<NewsRequestDto, NewsResponseDto, Long> newsController,
                            @Qualifier("authorController")
                            BaseController<AuthorRequestDto, AuthorResponseDto, Long> authorController) {
        this.newsController = newsController;
        this.authorController = authorController;
    }

    public void executeCommand(CommandType commandType) throws Throwable {
        if (commandType == CommandType.EXIT)
            System.exit(0);
        System.out.print("Operation: ");
        System.out.println(commandType.description);
        Command command = getCommandClassImpl(commandType);
        command.execute();
    }

    private Command getCommandClassImpl(CommandType commandType) {
        return switch (commandType) {
            case GET_ALL_NEWS -> new ReadAllNewsCommand(newsController);
            case GET_NEWS_BY_ID -> new ReadNewsByIdCommand(newsController, requestNewsId());
            case CREATE_NEWS -> new CreateNewsCommand(newsController,
                    new NewsRequestDto(
                            null,
                            requestNewsTitle(),
                            requestNewsContent(),
                            requestAuthorId()));
            case UPDATE_NEWS -> new UpdateNewsCommand(newsController,
                    new NewsRequestDto(
                            requestNewsId(),
                            requestNewsTitle(),
                            requestNewsContent(),
                            requestAuthorId()));
            case REMOVE_NEWS_BY_ID -> new DeleteNewsCommand(newsController, requestNewsId());
            case GET_ALL_AUTHORS -> new ReadAllAuthorsCommand(authorController);
            case GET_AUTHOR_BY_ID -> new ReadAuthorByIdCommand(authorController, requestAuthorId());
            case CREATE_AUTHOR -> new CreateAuthorCommand(authorController,
                    new AuthorRequestDto(
                            null,
                            requestAuthorName()));
            case UPDATE_AUTHOR -> new UpdateAuthorCommand(authorController,
                    new AuthorRequestDto(
                            requestAuthorId(),
                            requestAuthorName()));
            case REMOVE_AUTHOR_BY_ID -> new DeleteAuthorCommand(authorController, requestAuthorId());
            default -> throw new IllegalStateException("Unexpected commandType: " + commandType);
        };
    }

    private long requestNewsId() {
        try {
            return Long.parseLong(commandsReader.requestResponseByPrompt("Enter news id:"));
        } catch (NumberFormatException e) {
            throw new IdFormatException(
                    "ERROR_CODE: 05 ERROR_MESSAGE: News id should be number");
        }
    }

    private long requestAuthorId() {
        try {
            return Long.parseLong(commandsReader.requestResponseByPrompt("Enter author id:"));
        } catch (NumberFormatException e) {
            throw new IdFormatException(
                    "ERROR_CODE: 05 ERROR_MESSAGE: Author id should be number");
        }
    }

    private String requestNewsContent() {
        return commandsReader.requestResponseByPrompt("Enter news content:");
    }

    private String requestNewsTitle() {
        return commandsReader.requestResponseByPrompt("Enter news title:");
    }

    private String requestAuthorName() {
        return commandsReader.requestResponseByPrompt("Enter author name:");
    }

}
