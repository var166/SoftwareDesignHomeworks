package andrei.usersservice.command;

import andrei.usersservice.service.IUserCommandService;

public class UpdateUserRoleCommand implements Command {

    private final IUserCommandService receiver;
    private final long id;
    private final String userRole;

    public UpdateUserRoleCommand(IUserCommandService receiver, long id, String userRole) {
        this.receiver = receiver;
        this.id = id;
        this.userRole = userRole;
    }

    @Override
    public void execute() {
        receiver.updateUserRole(id, userRole);
    }
}
