package ru.vyukov.portto.porttoserver.sshd.command;

import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.command.CommandFactory;
import org.springframework.stereotype.Service;

/**
 * @author Oleg Vyukov
 */
@Service
public class StatsCommandFactory implements CommandFactory  {
    @Override
    public Command createCommand(String command) {
        return null;
    }
}
