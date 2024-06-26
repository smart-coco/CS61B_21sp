package gitlet;

/**
 * Driver class for Gitlet, a subset of the Git version-control system.
 * 
 * @author TODO
 */
public class Main {

    /**
     * Usage: java gitlet.Main ARGS, where ARGS contains
     * <COMMAND> <OPERAND1> <OPERAND2> ...
     */
    public static void main(String[] args) {
        // If args is empty?
        if (args.length == 0) {
            System.out.println("Need argument");
            return;
        }
        String firstArg = args[0];
        switch (firstArg) {
            case "init":
                Repository.init();
                break;
            case "add":
                Repository.add(args[1]);
                break;
            case "commit":
                if (args.length != 2) {
                    System.out.println("Please enter a commit message.");
                    return;
                }
                Repository.commit(args[1]);
                break;
            case "rm":
                Repository.rm(args[1]);
                break;
            case "log":
                Repository.log();
                break;
            case "global-log":
                // TODO: handle the `add [filename]` command
                break;
            case "find":
                // TODO: handle the `add [filename]` command
                break;
            case "status":
                Repository.status();
                break;
            case "checkout":
                if (args.length == 2) {
                    Repository.checkout(args[1]);
                }
                if (args.length == 3) {
                    Repository.checkout(args[1], args[2]);
                }
                if (args.length == 4) {
                    Repository.checkout(args[1], args[2], args[3]);
                }
                break;
            case "branch":
                // TODO: handle the `add [filename]` command
                break;
            case "rm-branch":
                // TODO: handle the `add [filename]` command
                break;
            case "reset":
                // TODO: handle the `add [filename]` command
                break;
            case "merge":
                // TODO: handle the `add [filename]` command
                break;
            default:
                System.out.println(
                        "Need operation:init/add/commit/rm/log/global-log/find/status/checkout/branch/rm-branch/reset/merge");
        }
    }
}
