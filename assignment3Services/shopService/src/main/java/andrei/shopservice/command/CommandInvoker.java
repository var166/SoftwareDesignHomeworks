package andrei.shopservice.command;

import org.springframework.stereotype.Service;

@Service
public class CommandInvoker {

    public void execute(Command command) {
        command.execute();
    }
}
