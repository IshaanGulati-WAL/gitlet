package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/** Driver class for Gitlet, the tiny stupid version-control system.
 * Collaborators: Kunal Singh and Sai Mandava
 *  @author Santhosh Subramanain
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args)
            throws IOException, ClassNotFoundException {
        ArrayList<String> operands = new ArrayList<>();
        if (args.length == 0 || args == null) {
            System.out.println("Please enter a command");
            System.exit(0);
        }
        operands.addAll(Arrays.asList(args));
        Gitlet commands = new Gitlet();
        commands.main(operands);
        Utils.writeContents(new File(".gitlet/gitlet"),
                Utils.serialize(commands));
    }


}
