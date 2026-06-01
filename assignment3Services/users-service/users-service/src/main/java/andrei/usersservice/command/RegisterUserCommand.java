package andrei.usersservice.command;

import andrei.usersservice.service.IUserCommandService;

public class RegisterUserCommand implements Command {

    private final IUserCommandService receiver;
    private final String username;
    private final String email;
    private final String password;

    public RegisterUserCommand(IUserCommandService receiver, String username, String email, String password) {
        this.receiver = receiver;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public void execute() {
        receiver.register(username, email, password);
    }
}
