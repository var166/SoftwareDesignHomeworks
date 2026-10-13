package andrei.usersservice.command;

import andrei.usersservice.service.IUserCommandService;

public class DeleteUserCommand implements Command {

    private final IUserCommandService receiver;
    private final long id;

    public DeleteUserCommand(IUserCommandService receiver, long id) {
        this.receiver = receiver;
        this.id = id;
    }

    @Override
    public void execute() {
        receiver.delete(id);
    }
}
