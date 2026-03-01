package ru.gentleman.user.command.interceptor;

import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.stereotype.Component;
import ru.gentleman.commom.util.ExceptionUtils;
import ru.gentleman.user.command.CreateUserCommand;
import ru.gentleman.user.command.DeleteUserCommand;
import ru.gentleman.user.command.UpdateUserCommand;
import ru.gentleman.user.service.UserService;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BiFunction;

@Component
@RequiredArgsConstructor
public class UserCommandInterceptor  implements MessageDispatchInterceptor<CommandMessage<?>>  {

    private final UserService userService;

    @Nonnull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(@Nonnull List<? extends CommandMessage<?>> messages) {
        return (index, command) -> {
            if(CreateUserCommand.class.equals(command.getPayloadType())) {
                CreateUserCommand createUserCommand = (CreateUserCommand) command.getPayload();

                if(this.userService.existsUserByEmail(createUserCommand.email())){
                    ExceptionUtils.alreadyExists("error.details.already_exist", createUserCommand.email());
                }
            } else if (UpdateUserCommand.class.equals(command.getPayloadType())) {
                UpdateUserCommand updateUserCommand = (UpdateUserCommand) command.getPayload();

                if(!this.userService.existsById(updateUserCommand.id())){
                    ExceptionUtils.alreadyExists("error.details.not_found", updateUserCommand.id());
                }
            }  else if (DeleteUserCommand.class.equals(command.getPayloadType())) {
                DeleteUserCommand deleteUserCommand = (DeleteUserCommand) command.getPayload();

                if(!this.userService.existsById(deleteUserCommand.id())){
                    ExceptionUtils.alreadyExists("error.details.not_found", deleteUserCommand.id());
                }
            }

            return command;
        };
    }
}
