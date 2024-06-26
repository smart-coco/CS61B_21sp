package gitlet;

import java.io.Serializable;

// TODO: any imports you need here

import java.util.Date; // TODO: You'll likely use this in this class
import java.util.HashMap;
import java.util.Map;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static gitlet.Utils.*;

/**
 * Represents a gitlet commit object.
 * TODO: It's a good idea to give a description here of what else this Class
 * does at a high level.
 *
 * @author Wangkh
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    private Date timestamp;
    private String parent;
    private Map<String, String> file_map;
    private String sha1;

    // first commit
    public Commit() {
        this.message = "initial commit";
        this.timestamp = new Date(0);
        this.parent = "";
        file_map = new HashMap<>();

        this.sha1 = Utils.sha1(message, timestamp.toString(), parent, file_map.toString());
    }

    // commit structor
    public Commit(String message) {
        this.message = message;
        this.timestamp = new Date();

        // get head commit
        String head_commit_sha1 = readContentsAsString(Repository.HEAD_FILE);
        this.parent = head_commit_sha1;
        Commit head_commit = readObject(join(Repository.COMMIT_DIR, head_commit_sha1), Commit.class);

        // copy file_map from head commit
        file_map = new HashMap<>(head_commit.get_file_map());

        // get stage class
        Stage stage = readObject(Repository.STAGE_FILE, Stage.class);
        Map<String, String> stage_addtion = stage.get_addtion();
        Map<String, String> stage_removal = stage.get_removal();

        // iterate addtion map of stage
        for (String key : stage_addtion.keySet()) {
            file_map.put(key, stage_addtion.get(key));
        }

        // iterate removal map of stage
        for (String key : stage_removal.keySet()) {
            file_map.remove(key);
        }

        // compute sha1
        this.sha1 = Utils.sha1(message, timestamp.toString(), parent, file_map.toString());

    }

    // get timestamp
    public String get_timestamp() {
        // dateformat
        DateFormat df = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z");
        return df.format(this.timestamp);
    }

    // get sha1
    public String get_sha1() {
        return this.sha1;
    }

    // get message
    public String get_message() {
        return this.message;
    }

    // get parent
    public String get_parent() {
        return this.parent;
    }

    // get file_map
    public Map<String, String> get_file_map() {
        return this.file_map;
    }
}
