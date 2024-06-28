package gitlet;

import java.io.File;
import java.util.Map;
import java.util.List;

import static gitlet.Utils.*;

// TODO: any imports you need here

/**
 * Represents a gitlet repository.
 * TODO: It's a good idea to give a description here of what else this Class
 * does at a high level.
 *
 * @author Wangkh
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    // blob directory
    public static final File BLOB_DIR = join(GITLET_DIR, "blob");
    // commit directory
    public static final File COMMIT_DIR = join(GITLET_DIR, "commit");
    // stage file
    public static final File STAGE_FILE = join(GITLET_DIR, "stage");
    // branch directory
    public static final File BRANCH_DIR = join(GITLET_DIR, "branch");
    // head file
    public static final File HEAD_FILE = join(GITLET_DIR, "head");

    // method
    // init
    public static void init() {
        // If a Gitlet version-control system already exists in the current directory.
        if (GITLET_DIR.exists() && GITLET_DIR.isDirectory()) {
            System.out.println("A Gitlet version-control system already exists in the current directory");
            System.exit(0);
        }
        // create directory
        GITLET_DIR.mkdir();
        BLOB_DIR.mkdir();
        COMMIT_DIR.mkdir();
        BRANCH_DIR.mkdir();

        // new stage and consist
        Stage stage = new Stage();
        writeObject(STAGE_FILE, stage);

        // new commit and consist
        Commit commit = new Commit();
        writeObject(join(COMMIT_DIR, commit.get_sha1()), commit);

        // new branch and consist
        writeContents(join(BRANCH_DIR, "master"), commit.get_sha1());

        // new head and consist
        writeContents(HEAD_FILE, "master");
    }

    // add
    public static void add(String file_name) {
        // if file exists?
        // Yes:get file_sha1
        // No:print error message and exit
        File file_path = join(CWD, file_name);
        if (!file_path.exists()) {
            System.out.println("File does not exist");
            return;
        }
        String file_sha1 = sha1(readContents(file_path));

        // make a blob and point to it sha1
        writeContents(join(BLOB_DIR, file_sha1), readContents(file_path));

        // if name exist in stage ?
        // Yes: override
        // No: add to dic
        Stage stage = readObject(STAGE_FILE, Stage.class);
        stage.store_addtion(file_name, file_sha1);

        // get active_commit
        String head_branch = readContentsAsString(HEAD_FILE);
        String active_commit = readContentsAsString(join(BRANCH_DIR, head_branch));
        Commit commit = readObject(join(COMMIT_DIR, active_commit), Commit.class);

        // if key:value equal to current commit version
        // Yes:do not stage and remove
        Map temp_map = commit.get_file_map();
        if (temp_map.containsKey(file_name) && temp_map.get(file_name) == file_sha1) {
            stage.remove_addtion(file_name);
        }

        // write stage
        writeObject(STAGE_FILE, stage);
    }

    // commit
    public static void commit(String message) {
        // get commit class
        // String active_commit = readContentsAsString(HEAD_FILE);
        // Commit commit = readObject(join(COMMIT_DIR, active_commit), Commit.class);

        // get stage class
        Stage stage = readObject(STAGE_FILE, Stage.class);

        // stage is empty?
        if (stage.get_addtion().size() == 0 && stage.get_removal().size() == 0) {
            System.out.println("No changes added to the commit.");
            return;
        }

        // make a new commit
        Commit new_commit = new Commit(message);

        // change branch
        String head_branch = readContentsAsString(HEAD_FILE);
        writeContents(join(BRANCH_DIR, head_branch), new_commit.get_sha1());

        // new_commit consist
        writeObject(join(COMMIT_DIR, new_commit.get_sha1()), new_commit);

        // restore stage
        writeObject(STAGE_FILE, new Stage());
    }

    // rm
    public static void rm(String file_name) {
        // get stage
        Stage stage = readObject(STAGE_FILE, Stage.class);

        // get active_commit
        String head_branch = readContentsAsString(HEAD_FILE);
        String active_commit = readContentsAsString(join(BRANCH_DIR, head_branch));
        Commit commit = readObject(join(COMMIT_DIR, active_commit), Commit.class);

        // If the file is neither staged nor tracked by the head commit
        // print the error message:No reason to remove the file.
        // and return
        if (!stage.get_addtion().containsKey(file_name) && !commit.get_file_map().containsKey(file_name)) {
            System.out.println("No reason to remove the file.");
            return;
        }

        // If the file is staged,remove
        if (stage.get_addtion().containsKey(file_name)) {
            stage.get_addtion().remove(file_name);
        }

        // If the file is in active_commit,add to removal
        if (commit.get_file_map().containsKey(file_name)) {
            // get file sha1
            File file_path = join(CWD, file_name);
            String sha1_current = sha1(readContents(file_path));
            // add to removal of stage
            stage.store_removal(file_name, sha1_current);
            // if file in active_commit sha1 == current file,remove it from work directory
            if (file_path.exists()) {
                String sha1_commit = commit.get_file_map().get(file_name);
                if (sha1_current == sha1_commit) {
                    file_path.delete();
                }
            }
        }

        // write stage
        writeObject(STAGE_FILE, stage);
    }

    // log
    public static void log() {
        // get head commit
        String head_branch = readContentsAsString(HEAD_FILE);
        String point_commit_sha1 = readContentsAsString(join(BRANCH_DIR, head_branch));

        // repeat get parent commit until null
        while (!point_commit_sha1.equals("")) {
            // get commit and print sha1,date,message
            Commit commit = readObject(join(COMMIT_DIR, point_commit_sha1), Commit.class);
            System.out.println("===");
            System.out.println("commit" + " " + point_commit_sha1);
            System.out.println("Date:" + " " + commit.get_timestamp());
            System.out.println(commit.get_message());
            System.out.println();

            // get next commit sha1
            point_commit_sha1 = commit.get_parent();
        }
    }

    // global-log
    public static void global_log() {
        // get list of commit
        List<String> commit_list = plainFilenamesIn(COMMIT_DIR);

        // iterate and print out
        for (String commit_sha1 : commit_list) {
            Commit commit = readObject(join(COMMIT_DIR, commit_sha1), Commit.class);
            System.out.println("===");
            System.out.println("commit" + " " + commit_sha1);
            System.out.println("Date" + " " + commit.get_timestamp());
            System.out.println(commit.get_message());
            System.out.println();
        }
    }

    // find
    public static void find(String message) {
        // get list of commit
        List<String> commit_list = plainFilenamesIn(COMMIT_DIR);

        // iterate and print out
        for (String commit_sha1 : commit_list) {
            Commit commit = readObject(join(COMMIT_DIR, commit_sha1), Commit.class);
            if (commit.get_message().equals(message)) {
                System.out.println(commit_sha1);
            }
        }
    }

    // status
    public static void status() {
        // show branch
        System.out.println("=== Branches ===");
        // get head commit
        String head_branch = readContentsAsString(HEAD_FILE);
        String head_commit = readContentsAsString(join(BRANCH_DIR, head_branch));

        List<String> branch_list = plainFilenamesIn(BRANCH_DIR);

        for (String item : branch_list) {
            if (head_commit.equals(readContentsAsString(join(BRANCH_DIR, item)))) {
                System.out.println("*" + item);
            } else {
                System.out.println(item);
            }
        }
        System.out.println();

        // get stage
        Stage stage = readObject(STAGE_FILE, Stage.class);

        // show Stage file
        System.out.println("=== Staged Files ===");
        Map<String, String> stage_addtion = stage.get_addtion();
        for (String key : stage_addtion.keySet()) {
            System.out.println(key);
        }
        System.out.println();

        // show removed file
        System.out.println("=== Removed Files ===");
        Map<String, String> stage_removal = stage.get_removal();
        for (String key : stage_removal.keySet()) {
            System.out.println(key);
        }
        System.out.println();

        // get current commit class
        Commit commit = readObject(join(COMMIT_DIR, head_commit), Commit.class);

        // get current file list
        List<String> current_list = plainFilenamesIn(CWD);

        // show motified but not staged
        System.out.println("=== Modifications Not Staged For Commit ===");
        // iterate CWD to compare file within with commit file map
        for (String item : current_list) {
            String file_sha1 = sha1(readContents(join(CWD, item)));
            String commit_file_sha1 = commit.get_file_map().get(item);
            String stage_addtion_sha1 = stage.get_addtion().get(item);
            String stage_removal_sha1 = stage.get_removal().get(item);
            // Tracked in the current commit, changed in the working directory, but not
            // not staged;
            if (commit_file_sha1 != null && (!file_sha1.equals(commit_file_sha1)
                    && !file_sha1.equals(stage_addtion_sha1))) {
                System.out.println(item);
            }

            // Staged for addition, but with different contents than in the working
            // directory
            if (stage_addtion_sha1 != null && !stage_addtion_sha1.equals(file_sha1)) {
                System.out.println(item);
            }
        }
        // Staged for addition, but deleted in the working directory
        for (String item2 : stage.get_addtion().keySet()) {
            File current_file = join(CWD, item2);
            if (!current_file.exists()) {
                System.out.println(item2);
            }
        }
        // Not staged for removal, but tracked in the current commit and deleted from
        // the working directory.
        for (String item : commit.get_file_map().keySet()) {
            if (stage.get_removal().get(item) == null && !join(CWD, item).exists()) {
                System.out.println(item);
            }
        }

        System.out.println();

        // show untracked file
        System.out.println("=== Untracked Files ===");
        // iterate CWD present in the working directory but neither staged for
        // addition nor tracked.
        for (String item : current_list) {
            if (!commit.get_file_map().containsKey(item) && !stage.get_addtion().containsKey(item)) {
                System.out.println(item);
            }
        }

        System.out.println();
    }

    // checkout 1
    public static void checkout(String branch_name) {

    }

    // checkout 2
    public static void checkout(String label, String file_name) {
        // get current commit class
        String head_branch = readContentsAsString(HEAD_FILE);
        String head_commit = readContentsAsString(join(BRANCH_DIR, head_branch));
        Commit commit = readObject(join(COMMIT_DIR, head_commit), Commit.class);

        // if not contain file_name return
        if (!commit.get_file_map().containsKey(file_name)) {
            return;
        }

        // overrwite cwd file
        // get file sha1
        String file_sha1 = commit.get_file_map().get(file_name);

        // read file from blob
        String file_content = readContentsAsString(join(BLOB_DIR, file_sha1));

        // write content to file
        writeContents(join(CWD, file_name), file_content);
    }

    // checkout 3
    public static void checkout(String commit_sha1, String label, String file_name) {
        // get commit class
        Commit commit = readObject(join(COMMIT_DIR, commit_sha1), Commit.class);

        // if not contain file_name return
        if (!commit.get_file_map().containsKey(file_name)) {
            return;
        }

        // overrwite cwd file
        // get file sha1
        String file_sha1 = commit.get_file_map().get(file_name);

        // read file from blob
        String file_content = readContentsAsString(join(BLOB_DIR, file_sha1));

        // write content to file
        writeContents(join(CWD, file_name), file_content);
    }

    // branch
    public static void branch(String branch_name) {
        // If a branch with the given name already exists
        List<String> branch_list = plainFilenamesIn(BRANCH_DIR);
        for (String item : branch_list) {
            if (item.equals(branch_name)) {
                System.out.println("A branch with that name already exists.");
                return;
            }
        }

        // get current commit class
        String head_branch = readContentsAsString(HEAD_FILE);
        String head_commit = readContentsAsString(join(BRANCH_DIR, head_branch));
        Commit commit = readObject(join(COMMIT_DIR, head_commit), Commit.class);

        // make a new branch
        writeContents(join(BRANCH_DIR, branch_name), commit.get_sha1());
    }

    // rm-branch
    public static void rm_branch(String branch_name) {
        // If a branch with the given name does not exist
        if (!join(BRANCH_DIR, branch_name).exists()) {
            System.out.println("A branch with that name does not exist.");
        }

        // If you try to remove the branch you’re currently on
        if (readContentsAsString(HEAD_FILE).equals(branch_name)) {
            System.out.println("Cannot remove the current branch.");
        }

        // delete branch
        join(BRANCH_DIR, branch_name).delete();
    }
}
