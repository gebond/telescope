package org.hackday.telescope.commands;

import java.util.concurrent.Callable;

public interface Command extends Callable<CommandResult> {


}
