package gitlet;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Commit class.
 *
 * @author Tosh
 **/
public class Commit implements Serializable {
    /**
     * message.
     **/
    private String _message;
    /**
     * blobs.
     **/
    private LinkedHashMap<String, Blob> _blobs = new LinkedHashMap<>();
    /**
     * time.
     **/
    private String _time;
    /**
     * initial hash.
     **/
    private String _initialhash;
    /**
     * names.
     **/
    private ArrayList<String> _names = new ArrayList<>();
    /**
     * commithash.
     **/
    private String _commithash;
    /**
     * branch.
     **/
    private String _branch;
    /**
     * parentid.
     **/
    private String _parentid;
    /**
     * merge.
     **/
    private Boolean _merge;
    /**
     * mergeparent.
     **/
    private String _mergeparent;
    /**
     * mergehash.
     **/
    private String _mergehash;
    /**
     * mergeid.
     **/
    private ArrayList<String> _mergeid = new ArrayList<>();
    /**
     * contents.
     **/
    private ArrayList<String> _contents = new ArrayList<>();


    /**
     * Commit constructor inputs
     * MESSAGE, PARENTID, BLOBS, and BRANCH.
     **/
    Commit(String message, String parentid,
           LinkedHashMap<String, Blob> blobs, String branch) {
        _message = message;
        _parentid = parentid;
        _blobs = blobs;
        _branch = branch;
        _merge = false;
        for (String s : blobs.keySet()) {
            _names.add(blobs.get(s).getname());
            _contents.add(blobs.get(s).getcontentsasstring());
        }

        ZonedDateTime now = ZonedDateTime.now();
        _time = now.format
                (DateTimeFormatter.ofPattern
                        ("EEE MMM d HH:mm:ss yyyy xxxx"));
        _commithash = commithash();
    }

    /**
     * Another commit constructor input STRING, BRANCH.
     **/
    Commit(String string, String branch) {
        _branch = branch;
        _message = string;
        _time = "Wed Dec 31 16:00:00 1969 -0800";
        _parentid = null;
        _merge = false;
        _initialhash = initialhash();
    }

    /**
     * input MESSAGE,
     * CURRENTBRANCHID, BLOBS,
     * CHOSENBRANCHID, BRANCH.
     **/
    Commit(String message,
           String currentbranchid,
           String chosenbranchid,
           LinkedHashMap<String, Blob> blobs,
           String branch) {
        _message = message;
        ZonedDateTime now = ZonedDateTime.now();
        _time = now.format
                (DateTimeFormatter.ofPattern
                        ("EEE MMM d HH:mm:ss yyyy xxxx"));
        _merge = true;
        for (String s : blobs.keySet()) {
            _names.add(blobs.get(s).getname());
            _contents.add(blobs.get(s).getcontentsasstring());
        }
        _mergeid.add(currentbranchid);
        _mergeid.add(chosenbranchid);
        _mergeparent = currentbranchid;
        _mergehash = mergehash();
        _branch = branch;
    }

    /**
     * returns MESSAGE.
     **/
    String getmessage() {
        return _message;
    }

    /**
     * returns TIME.
     **/
    String gettime() {
        return _time;
    }

    /**
     * returns PARENTID.
     **/
    String getparentid() {
        return _parentid;
    }

    /**
     * returns HASH.
     **/
    String gethash() {
        if ((_message).equals("initial commit")) {
            return _initialhash;
        } else if (_merge) {
            return _mergehash;
        } else {
            return _commithash;
        }
    }

    /**
     * return MERGEPARENT.
     **/
    String getmergeparent() {
        return _mergeparent;
    }

    /**
     * return MERGEID.
     **/
    ArrayList<String> getmergeid() {
        return _mergeid;
    }

    /**
     * return MERGE boolean.
     **/
    Boolean merge() {
        return _merge;
    }

    /**
     * returns NAMES.
     **/
    ArrayList<String> getnames() {
        return _names;
    }

    /**
     * returns BRANCH.
     **/
    String getbranch() {
        return _branch;
    }

    /**
     * returns BLOBS.
     **/
    LinkedHashMap<String, Blob> getblobs() {
        return _blobs;
    }

    /**
     * hash generator returns HASH.
     **/
    private String commithash() {
        List<Object> commitcontents = new ArrayList<>();
        commitcontents.add("commit");
        commitcontents.add(_message);
        commitcontents.add(_branch);
        commitcontents.addAll(_names);
        commitcontents.addAll(_contents);
        if (!(_parentid == null)) {
            commitcontents.add(_parentid);
        }
        return Utils.sha1(commitcontents);
    }

    /** return HASH for merge commit. **/
    private String mergehash() {
        List<Object> commitcontents = new ArrayList<>();
        commitcontents.add("commit");
        commitcontents.add(_message);
        commitcontents.addAll(_mergeid);
        commitcontents.add(_mergeparent);
        commitcontents.addAll(_names);
        commitcontents.addAll(_contents);
        return Utils.sha1(commitcontents);
    }

    /**
     * hash generator returns of initial HASH.
     **/
    private String initialhash() {
        List<Object> initial = new ArrayList<>();
        initial.add("commit");
        initial.add(_time);
        initial.add(_message);
        return Utils.sha1(initial);
    }
}
