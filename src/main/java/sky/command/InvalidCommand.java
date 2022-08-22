package sky.command;

import sky.Storage;
import sky.TaskList;
import sky.exception.TextNoMeaningException;
import sky.Ui;

/**
 * The InvalidCommand class deals with invalid inputs.
 */
public class InvalidCommand extends Command {
    @Override
    public String execute(TaskList taskList, Ui ui, Storage storage) throws TextNoMeaningException {
        throw new TextNoMeaningException("  Are you new? Type a command that I actually know.");
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
