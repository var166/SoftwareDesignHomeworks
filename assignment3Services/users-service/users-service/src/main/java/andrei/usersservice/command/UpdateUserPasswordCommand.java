package andrei.usersservice.command;

import andrei.usersservice.service.IUserCommandService;

public class UpdateUserPasswordCommand implements Command {

    private final IUserCommandService receiver;
    private final long id;
    private final String password;

    public UpdateUserPasswordCommand(IUserCommandService receiver, long id, String password) {
        this.receiver = receiver;
        this.id = id;
        this.password = password;
    }

    @Override
    public void execute() {
        receiver.updatePassword(id, password);
    }
}
