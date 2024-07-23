package gitlet;

import java.io.File;
import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

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

        // if name exist in stage addtion?
        // Yes: override
        // No: add to dic
        Stage stage = readObject(STAGE_FILE, Stage.class);
        stage.store_addtion(file_name, file_sha1);

        // if name exist in stage removal remove it
        stage.remove_removal(file_name);

        // get active_commit
        String head_branch = readContentsAsString(HEAD_FILE);
        String active_commit = readContentsAsString(join(BRANCH_DIR, head_branch));
        Commit commit = readObject(join(COMMIT_DIR, active_commit), Commit.class);

        // if key:value equal to current commit version
        // Yes:do not stage and remove
        Map temp_map = commit.get_file_map();
        if (temp_map.containsKey(file_name) && temp_map.get(file_name).equals(file_sha1)) {
            stage.remove_addtion(file_name);
        }

        // write stage
        writeObject(STAGE_FILE, stage);
    }

    // commit
    public static void commit(String message) {
        // get stage class
        Stage stage = readObject(STAGE_FILE, Stage.class);

        // message is blank?
        if (message.equals("")) {
            System.out.println("Please enter a commit message.");
            return;
        }

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
            // add to removal of stage
            stage.store_removal(file_name, null);
            // if file in active_commit sha1 == current file,remove it from work directory
            if (file_path.exists()) {
                file_path.delete();
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
            System.out.println("Date:" + " " + commit.get_timestamp());
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
            if (!stage.get_removal().containsKey(item) && !join(CWD, item).exists()) {
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

    // reset
    public static void reset(String commit_sha1) {
        // get class of commit_sha1 If no commit with the given id exists, print No
        // commit with that id exists.
        if (!join(COMMIT_DIR, commit_sha1).exists()) {
            System.out.println("No commit with that id exists.");
            return;
        }
        Commit target_commit = readObject(join(COMMIT_DIR, commit_sha1), Commit.class);

        // get class of current head branch
        String head_branch = readContentsAsString(HEAD_FILE);
        String head_commit_sha1 = readContentsAsString(join(BRANCH_DIR, head_branch));
        Commit head_commit = readObject(join(COMMIT_DIR, head_commit_sha1), Commit.class);

        // If a working file is untracked in the current branch and would be
        // overwritten by the reset, print `There is an untracked file in the way;
        // delete it, or add and commit it first.`
        List<String> cwd_file_list = plainFilenamesIn(CWD);
        for (String item : cwd_file_list) {
            // meet the condition
            if (!head_commit.get_file_map().containsKey(item) && target_commit.get_file_map().containsKey(item)) {
                String current_file_sha1 = sha1(readContents(join(CWD, item)));
                String target_file_sha1 = target_commit.get_file_map().get(item);
                if (current_file_sha1.equals(target_file_sha1)) {
                    System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                    return;
                }
            }
        }

        // Checks out all the files tracked by the given commit
        for (String item : target_commit.get_file_map().keySet()) {
            Repository.checkout(head_commit_sha1, "--", item);
        }

        // Removes tracked files that are not present in that commit
        for (String item : cwd_file_list) {
            // meet the condition
            if (head_commit.get_file_map().containsKey(item) && !target_commit.get_file_map().containsKey(item)) {
                join(CWD, item).delete();
            }
        }

        // moves the current branch’s head to that commit node
        writeContents(join(BRANCH_DIR, head_branch), commit_sha1);

        // restore stage
        writeObject(STAGE_FILE, new Stage());
    }

    // merge
    public static void merge(String branch_name) {
        // fail case
        // If a branch with the given name does not exist,
        if (!join(BRANCH_DIR, branch_name).exists()) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        // If there are staged additions or removals present
        Stage stage = readObject(STAGE_FILE, Stage.class);
        if (stage.get_addtion().size() != 0 || stage.get_removal().size() != 0) {
            System.out.println("You have uncommitted changes.");
            return;
        }
        // If attempting to merge a branch with itself
        if (readContentsAsString(HEAD_FILE).equals(branch_name)) {
            System.out.println("Cannot merge a branch with itself.");
            return;
        }
        // TODO If an untracked file in the current commit would be overwritten or
        // deleted by the merge

        // get class of split point_commit
        // Step1 get the set of current branch
        Set<String> ancestor_commit = new HashSet<String>();
        // get head commit
        String head_branch = readContentsAsString(HEAD_FILE);
        String current_commit_sha1 = readContentsAsString(join(BRANCH_DIR, head_branch));
        String point_commit_sha1 = readContentsAsString(join(BRANCH_DIR, head_branch));
        // repeat get parent commit until null
        while (!point_commit_sha1.equals("")) {
            ancestor_commit.add(point_commit_sha1);
            // get commit
            Commit commit = readObject(join(COMMIT_DIR, point_commit_sha1), Commit.class);
            // get next commit sha1
            point_commit_sha1 = commit.get_parent();
        }

        // Step2 iterate target branch until find commit in the set of current branch
        // get head commit
        String target_commit_sha1 = readContentsAsString(join(BRANCH_DIR, branch_name));
        point_commit_sha1 = readContentsAsString(join(BRANCH_DIR, branch_name));
        String split_commit_sha1 = null;
        // repeat get parent commit until null
        while (!point_commit_sha1.equals("")) {
            // if contains break
            if (ancestor_commit.contains(point_commit_sha1)) {
                split_commit_sha1 = point_commit_sha1;
                break;
            }
            // get commit
            Commit commit = readObject(join(COMMIT_DIR, point_commit_sha1), Commit.class);
            // get next commit sha1
            point_commit_sha1 = commit.get_parent();
        }

        // split point fail case
        // If the split point is the same commit as the given branch
        if (split_commit_sha1.equals(target_commit_sha1)) {
            System.out.println("Given branch is an ancestor of the current branch.");
            return;
        }
        // If the split point is the current branch
        if (split_commit_sha1.equals(current_commit_sha1)) {
            checkout(branch_name);
            System.out.println("Current branch fast-forwarded.");
            return;
        }

        // make a set of file and store their status in three commit
        Map<String, String> file_split_commit = new HashMap<>();
        Map<String, String> file_current_commit = new HashMap<>();
        Map<String, String> file_target_commit = new HashMap<>();
        Map<String, String> file_result_sha1 = new HashMap<>();

        Commit split_commit = readObject(join(COMMIT_DIR, point_commit_sha1), Commit.class);
        Commit current_commit = readObject(join(COMMIT_DIR, current_commit_sha1), Commit.class);
        Commit target_commit = readObject(join(COMMIT_DIR, target_commit_sha1), Commit.class);
        // iterate split point file set
        for (String item : split_commit.get_file_map().keySet()) {
            if (!file_split_commit.containsKey(item)) {
                file_current_commit.put(item, null);
                file_target_commit.put(item, null);
            }
            file_split_commit.put(item, split_commit.get_file_map().get(item));
        }
        // iterate current point file set
        for (String item : current_commit.get_file_map().keySet()) {
            if (!file_current_commit.containsKey(item)) {
                file_split_commit.put(item, null);
                file_target_commit.put(item, null);
            }
            file_current_commit.put(item, split_commit.get_file_map().get(item));
        }
        // iterate target point file set
        for (String item : target_commit.get_file_map().keySet()) {
            if (!file_target_commit.containsKey(item)) {
                file_split_commit.put(item, null);
                file_current_commit.put(item, null);
            }
            file_target_commit.put(item, split_commit.get_file_map().get(item));
        }

        // go through the set of file, and deal with then according to the rules
        for (String item : file_split_commit.keySet()) {
            // record result sha1
            String result_sha1 = null;
            // file_split_commit is not null
            if (file_split_commit.get(item) != null) {
                String file_split_commit_sha1 = file_split_commit.get(item);
                // rule 1
                if (file_target_commit.get(item) != null && file_split_commit_sha1 != file_target_commit.get(item)
                        && file_current_commit.get(item) == file_split_commit_sha1) {
                    result_sha1 = file_target_commit.get(item);
                }

                // rule 2
                if (file_current_commit.get(item) != null && file_split_commit_sha1 != file_current_commit.get(item)
                        && file_target_commit.get(item) == file_split_commit_sha1) {
                    result_sha1 = file_current_commit.get(item);
                }

                // TODO rule 3
                if (file_split_commit_sha1 != file_current_commit.get(item)
                        && file_split_commit_sha1 != file_target_commit.get(item)) {

                }
                // rule 6
                if (file_target_commit.get(item) == null
                        && file_current_commit.get(item) == file_split_commit.get(item)) {
                    result_sha1 = null;
                }
                // rule 7
                if (file_current_commit.get(item) == null
                        && file_target_commit.get(item) == file_split_commit.get(item)) {
                    result_sha1 = null;
                }
            }

            // file_split_commit is null
            if (file_split_commit.get(item) == null) {
                // rule 4
                if (file_target_commit.get(item) == null) {
                    result_sha1 = file_current_commit.get(item);
                }
                // rule 5
                if (file_current_commit.get(item) == null) {
                    result_sha1 = file_target_commit.get(item);
                }

                // rule 3 ec
                if (file_target_commit.get(item) != null && file_current_commit.get(item) != null) {
                }
            }
            // record result_sha1
            file_result_sha1.put(item, result_sha1);
        }

        // change the stage
        for (String item : file_result_sha1.keySet()) {
            String result_sha1 = file_result_sha1.get(item);
            if (result_sha1 != null && !result_sha1.equals(file_current_commit.get(item))) {
                stage.store_addtion(item, result_sha1);
            }
            if (result_sha1 == null && file_current_commit.get(item) != null) {
                stage.store_removal(item, null);
            }
        }
        // write stage
        writeObject(STAGE_FILE, stage);

        // commit
        Repository.commit("Merged " + branch_name + " into " + head_branch + ".");
    }
}
