package gitlet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;



/**
 * Gitlet Class.
 *
 * @author Santhosh Subramanian
 */
public class Gitlet implements Serializable {
    /**
     * current branch.
     **/
    private String _currentbranch;
    /**
     * current path.
     **/
    private String _currentpath;
    /**
     * commit hashmap.
     **/
    private LinkedHashMap<String, Commit> _commit;
    /**
     * branch hashmap.
     **/
    private LinkedHashMap<String, String> _branch;
    /**
     * staging hashmap.
     **/
    private LinkedHashMap<String, Blob> _staging;
    /**
     * command path.
     **/
    private String _command;
    /**
     * operand.
     **/
    private ArrayList<String> _operand = new ArrayList<>();
    /**
     * commitkeys.
     **/
    private ArrayList<String> _commitkeys = new ArrayList<>();
    /**
     * remove.
     **/
    private ArrayList<String> _remove = new ArrayList<>();
    /**
     * removebranch.
     **/
    private ArrayList<String> _removebranch = new ArrayList<>();
    /**
     * commitfilenames.
     **/
    private ArrayList<String> _commitfilenames = new ArrayList<>();
    /**
     * currentblobs.
     **/
    private LinkedHashMap<String, Blob> _currentblobs = new LinkedHashMap<>();
    /**
     * changes.
     **/
    private int _changes;
    /**
     * parentid.
     **/
    private String _parentid;
    /**
     * splitpoint.
     **/
    private Commit _splitpoint;
    /**
     * allbranch hashmap.
     **/
    private LinkedHashMap<String, ArrayList<Commit>> _allbranchcommits;
    /**
     * hash.
     **/
    private String _hash;
    /**
     * untracked files.
     */
    private LinkedHashMap<String, Blob> _untracked = new LinkedHashMap<>();

    /**
     * untracking files.
     **/
    private LinkedHashMap<String, Blob> _untracking = new LinkedHashMap<>();

    /**
     * filetracking.
     **/
    private HashMap<String, List<String>> _filetracking = new HashMap<>();
    /**
     * tracking.
     **/
    private ArrayList<String> _removedbuttracked = new ArrayList<>();
    /**
     * files.
     **/
    private List<String> _files = new ArrayList<>();

    /**
     * splitid.
     **/
    private HashMap<String, String> _splitid = new HashMap<>();
    /**
     * modified.
     **/
    private HashMap<String, String> _modified = new HashMap<>();
    /**
     * commit.
     **/
    private Commit _checkingcommit;


    /**
     * Gitlet.
     **/
    Gitlet() {
        File gitlet = new File(".gitlet/gitlet");
        if (gitlet.exists()) {
            Gitlet obj;
            try {
                ObjectInputStream in =
                        new ObjectInputStream(new FileInputStream(gitlet));
                obj = (Gitlet) in.readObject();
                in.close();
                makecopy(obj);
            } catch (IOException | ClassNotFoundException exception) {
                System.out.println(exception);
            }
        } else {
            _currentbranch = null;
            _currentpath = null;
            _staging = new LinkedHashMap<>();
            _commit = new LinkedHashMap<>();
            _branch = new LinkedHashMap<>();
            _commitkeys = new ArrayList<>();
            _remove = new ArrayList<>();
            _removebranch = new ArrayList<>();
            _currentblobs = new LinkedHashMap<>();
            _parentid = null;
            _allbranchcommits = new LinkedHashMap<>();
            _untracked = new LinkedHashMap<>();
            _commitfilenames = new ArrayList<>();
            _splitpoint = null;
            _hash = null;
            _changes = 0;
            _files = new ArrayList<>();
            _filetracking = new HashMap<>();
            _splitid = new HashMap<>();
            _modified = new HashMap<>();
            _untracking = new LinkedHashMap<>();
            _checkingcommit = null;
            _removedbuttracked = new ArrayList<>();

        }
    }

    /**
     * makes copy of GITLET.
     **/
    public void makecopy(Gitlet gitlet) {
        _currentpath = gitlet._currentpath;
        _commit = gitlet._commit;
        _branch = gitlet._branch;
        _staging = gitlet._staging;
        _commitkeys = gitlet._commitkeys;
        _currentbranch = gitlet._currentbranch;
        _currentblobs = gitlet._currentblobs;
        _remove = gitlet._remove;
        _removebranch = gitlet._removebranch;
        _parentid = gitlet._parentid;
        _allbranchcommits = gitlet._allbranchcommits;
        _splitpoint = gitlet._splitpoint;
        _hash = gitlet._hash;
        _commitfilenames = gitlet._commitfilenames;
        _changes = gitlet._changes;
        _untracked = gitlet._untracked;
        _filetracking = gitlet._filetracking;
        _files = gitlet._files;
        _splitid = gitlet._splitid;
        _modified = gitlet._modified;
        _untracking = gitlet._untracking;
        _checkingcommit = gitlet._checkingcommit;
        _removedbuttracked = gitlet._removedbuttracked;
    }

    /**
     * adds arguements to ARGUMENTS.
     **/
    public void firstarguments(ArrayList<String> arguments) {
        arguments.add("add");
        arguments.add("commit");
        arguments.add("log");
        arguments.add("global-log");
    }

    /**
     * adds arguements to ARGUMENTS.
     **/
    public void secondarguments(ArrayList<String> arguments) {
        arguments.add("checkout");
        arguments.add("status");
        arguments.add("rm");
        arguments.add("rm-branch");
    }

    /**
     * adds arguements to ARGUMENTS.
     **/
    public void thirdarguments(ArrayList<String> arguments) {
        arguments.add("find");
        arguments.add("merge");
        arguments.add("reset");
        arguments.add("branch");
    }

    /**
     * runs arguements given by STRING.
     **/
    public void main(ArrayList<String> string)
            throws IOException, ClassNotFoundException {
        ArrayList<String> command = string;
        ArrayList<String> firstarguments = new ArrayList<>();
        ArrayList<String> secondarguments = new ArrayList<>();
        ArrayList<String> thirdarguments = new ArrayList<>();

        firstarguments(firstarguments);
        secondarguments(secondarguments);
        thirdarguments(thirdarguments);

        _command = command.remove(0);
        _operand = command;
        if (_command.isEmpty()) {
            System.out.println("No command inputted");
            System.exit(0);
        }
        if (_command.equals("init")) {
            if (_operand.size() == 0) {
                init();
                return;
            } else {
                System.out.println("Incorrect Arguments");
            }
        }
        if (!thirdarguments.contains(_command)
                && !firstarguments.contains(_command)
                && !secondarguments.contains(_command)) {
            System.out.println("No command with that name exists");
            System.exit(0);
        }
        if (!Files.exists(Paths.get(".gitlet"))) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        if (firstarguments.contains(_command)) {
            first(_command, _operand);
        }
        if (secondarguments.contains(_command)) {
            second(_command, _operand);
        }
        if (thirdarguments.contains(_command)) {
            third(_command, _operand);
        }
    }

    /**
     * inputs COMMAND, OPERAND.
     **/
    private void third(String command, ArrayList<String> operand)
            throws IOException, ClassNotFoundException {
        if (_command.equals("find")) {
            if (_operand.size() == 1) {
                String message = _operand.get(0);
                find(message);
            } else {
                System.out.println("Incorrect Operands");
            }
        }
        if (_command.equals("merge")) {
            if (_operand.size() == 1) {
                String message = _operand.get(0);
                merge(message);
            } else {
                System.out.println("Incorrect Operands");
            }
        }
        if (_command.equals("branch")) {
            if (_operand.size() == 1) {
                String message = _operand.get(0);
                branch(message);
            } else {
                System.out.println("Incorrect Operands");
            }
        }
        if (_command.equals("reset")) {
            if (_operand.size() == 1) {
                String message = _operand.get(0);
                reset(message);
            } else {
                System.out.println("Incorrect Operands");
            }
        }
    }

    /**
     * inputs COMMAND, OPERAND.
     **/
    private void second(String command, ArrayList<String> operand)
            throws IOException, ClassNotFoundException {
        if (_command.equals("checkout")) {
            if (_operand.size() == 0 || _operand.size() > 3) {
                System.out.println("Incorrect operands.");
                System.exit(0);
            }
            checkout(_operand);
        }
        if (_command.equals("status")) {
            if (_operand.size() == 0) {
                status();
            } else {
                System.out.println("Incorrect Operands.");
            }
        }
        if (_command.equals("rm")) {
            String message = _operand.get(0);
            if (_operand.size() == 1) {
                rm(message);
            } else {
                System.out.println("Incorrect Operands");
            }
        }
        if (_command.equals("rm-branch")) {
            if (_operand.size() == 1) {
                String message = _operand.get(0);
                rmbranch(message);
            } else {
                System.out.println("Incorrect Operands");
            }
        }

    }

    /**
     * inputs COMMAND, OPERAND.
     **/
    private void first(String command, ArrayList<String> operand)
            throws IOException, ClassNotFoundException {
        if (_command.equals("add")) {
            if (_operand.size() == 1) {
                add(_operand.get(0));
            } else {
                System.out.println("Incorrect Argument");
            }
        }
        if (_command.equals("commit")) {
            if (_operand.size() == 1) {
                String message = _operand.get(0);
                commit(message);
            } else if (_operand.size() == 0) {
                System.out.println("Please enter a commit message.");
            } else {
                System.out.println("Incorrect Arguments");
            }
        }
        if (_command.equals("log")) {
            if (_operand.size() == 0) {
                log();
                return;
            } else {
                System.out.println("Incorrect Argument");
            }
        }
        if (_command.equals("global-log")) {
            if (_operand.size() == 0) {
                glog();
            } else {
                System.out.println("Incorrect Operands");
            }
        }
    }

    /**
     * init Method.
     **/
    public void init() throws IOException, ClassNotFoundException {
        if (!Files.exists(Paths.get(".gitlet"))) {
            Files.createDirectory(Paths.get(".gitlet"));
            Files.createDirectory(Paths.get(".gitlet/commits"));
            Files.createDirectory(Paths.get(".gitlet/staging"));
            Files.createDirectory(Paths.get(".gitlet/repo"));
            Files.createDirectory(Paths.get(".gitlet/merge"));
            branch("master");
            _currentbranch = "master";
            Commit commit = new Commit("initial commit", _currentbranch);
            _commit.put(commit.gethash(), commit);
            _parentid = commit.gethash();
            _commitkeys.add(_parentid);
            File newfile =
                    new File(".gitlet/commits/"
                            + commit.gethash());
            Utils.writeContents(newfile, Utils.serialize(commit));
        } else {
            System.out.println("A Gitlet version-control "
                    + "system already exists in the "
                    + "current directory");
        }
    }

    /**
     * add Method inputs FILENAME.
     **/
    public void add(String filename) throws IOException {
        if (!new File(filename).exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        if (_remove.contains(filename)) {
            _remove.remove(filename);
            _untracked.remove(filename);
            _removedbuttracked.add(filename);
            return;
        }
        Blob blob = new Blob(filename);
        Path path = Paths.get(".gitlet/commits/" + _parentid);
        Commit commit = (Commit) Utils.deserialize(path);
        for (String s : commit.getblobs().keySet()) {
            if (blob.gethash().
                    equals(commit.getblobs().get(s).gethash())) {
                if (_staging.containsKey(filename)) {
                    File oldfile =
                            new File("gitlet/staging/"
                                    + _staging.get(filename).gethash());
                    oldfile.delete();
                    _staging.remove(filename);
                    return;
                }
                return;
            }

        }
        if (!_parentid.equals(blob.gethash())) {
            if (!_staging.isEmpty() && _staging.containsKey(filename)) {
                File old = new File(".gitlet/staging/" + blob.gethash());
                old.delete();
                _staging.put(filename, blob);
                _currentblobs.put(filename, blob);
                File newfile = new File(".gitlet/staging/" + blob.gethash());
                Utils.writeContents(newfile, Utils.serialize(blob));
            }
            if (_staging.isEmpty() || !_staging.containsKey(filename)) {
                _staging.put(filename, blob);
                _currentblobs.put(filename, blob);
                File newfile = new File(".gitlet/staging/" + blob.gethash());
                Utils.writeContents(newfile, Utils.serialize(blob));
            }
        } else {
            File old = new File(".gitlet/staging/"
                    + _staging.get(filename).gethash());
            old.delete();
            _staging.remove(filename);
        }
    }

    /**
     * commit Method. inputs MESSAGE.
     **/
    private void commit(String message)
            throws IOException, ClassNotFoundException {
        if (_staging.size() == 0 && _changes == 0) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        if (message.equals("")) {
            System.out.println("Please enter a commit message.");
        }
        Commit commit =
                new Commit(message, _parentid, _currentblobs, _currentbranch);
        if (_parentid.equals(commit.gethash())) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        _branch.put(_currentbranch, commit.gethash());
        if (_allbranchcommits.containsKey(_currentbranch)) {
            _allbranchcommits.get(_currentbranch).add(commit);
        } else {
            _allbranchcommits.put(_currentbranch, new ArrayList<>());
            _allbranchcommits.get(_currentbranch).add(commit);
        }
        _commit.put(commit.gethash(), commit);
        _parentid = commit.gethash();
        _commitkeys.add(_parentid);
        _commitfilenames.addAll(_staging.keySet());
        File newfile = new File(".gitlet/commits/" + commit.gethash());
        Utils.writeContents(newfile, Utils.serialize(commit));
        File dir = new File(".gitlet/staging/");
        for (File file : dir.listFiles()) {
            if (!file.isDirectory()) {
                file.delete();
            }
        }
        _changes = 0;
        if (!_staging.isEmpty()) {
            _staging.clear();
        }
        _remove.clear();
        _removebranch.clear();
        _untracking.clear();
        _modified.clear();
    }

    /**
     * rm Method. inputs FILENAME
     **/
    private void rm(String filename)
            throws IOException, ClassNotFoundException {
        int count = 0;
        if (!(_staging.isEmpty())
                && _staging.containsKey(filename)) {
            File oldfile = new File(".gitlet/staging"
                    + _staging.get(filename).gethash());
            oldfile.delete();
            _staging.remove(filename);
            _currentblobs.remove(filename);
            _untracked.put(filename, new Blob(filename));
            count += 1;
            _changes += 1;
        }
        Commit commit = (Commit)
                Utils.deserialize(Paths.get(".gitlet/commits/" + _parentid));
        if (commit.getblobs().containsKey(filename)) {
            _currentblobs.remove(filename);
            _remove.add(filename);
            _removebranch.add(_currentbranch);
            if (_untracked.containsKey(filename)) {
                _untracked.remove(filename);
            }
            File oldfile = new File(filename);
            oldfile.delete();
            count += 1;
            _changes += 1;
        }
        if (count == 0) {
            System.out.println("No reason to remove the file.");
        }
    }

    /**
     * log Method.
     **/
    private void log() throws IOException, ClassNotFoundException {
        List<String> keys = new ArrayList<String>();
        Commit commit = (Commit)
                Utils.deserialize
                        (Paths.get(".gitlet/commits/"
                                + _branch.get(_currentbranch)));
        recursive(commit, keys);
        for (String string : keys) {
            Commit newcommit = _commit.get(string);
            System.out.println("===");
            System.out.println("commit " + string);
            if (newcommit.merge()) {
                System.out.println("Merge: "
                        + newcommit.getmergeid().get(0).substring(0, 7)
                        + " "
                        + newcommit.getmergeid().get(1).substring(0, 7));
            }
            System.out.println("Date: " + newcommit.gettime());
            System.out.println(newcommit.getmessage());
            System.out.println();
            if (newcommit.getparentid() == null
                    && newcommit.getmergeparent() == null) {
                return;
            }
        }

    }

    /**
     * input COMMIT, and KEYS, gets parentids.
     **/
    private void recursive(Commit commit, List<String> keys) {
        if (commit.getparentid() == null && commit.getmergeparent() == null) {
            keys.add(commit.gethash());
        }
        if (commit.getparentid() != null
                || commit.getmergeparent() != null) {
            keys.add(commit.gethash());
            if (commit.merge()) {
                commit = (Commit)
                        Utils.deserialize(Paths.get(".gitlet/commits/"
                                + commit.getmergeparent()));
            } else {
                commit = (Commit) Utils.deserialize(Paths.get(".gitlet/commits/"
                        + commit.getparentid()));
            }
            recursive(commit, keys);
        }
    }

    /**
     * glog Method.
     **/
    private void glog() throws IOException, ClassNotFoundException {
        List<String> keys =
                new ArrayList<String>(_commit.keySet());
        Collections.reverse(keys);
        for (String string : keys) {
            Commit commit = _commit.get(string);
            System.out.println("===");
            System.out.println("commit " + string);
            System.out.println("Date: " + commit.gettime());
            System.out.println(commit.getmessage());
            System.out.println();
        }
    }

    /**
     * find Method. inputs MESSAGE.
     **/
    private void find(String message)
            throws IOException, ClassNotFoundException {
        File[] files =
                new File(".gitlet/commits/").listFiles();
        int count = 0;
        for (int i = 0; i < files.length; i++) {
            Path dest = Paths.get(files[i].getPath());
            Commit commit = (Commit) Utils.deserialize(dest);
            if (commit.getmessage().equals(message)) {
                System.out.println(commit.gethash());
                count += 1;
            }
        }
        if (count == 0) {
            System.out.println("Found no commit with that message.");
        }
    }

    /**
     * status Method.
     **/
    private void status() throws IOException, ClassNotFoundException {
        checkuntrackedmodified();
        System.out.println("=== Branches ===");
        if (!_branch.isEmpty()) {
            for (String s : _branch.keySet()) {
                if (s.equals(_currentbranch)) {
                    System.out.println("*" + _currentbranch);
                } else {
                    System.out.println(s);
                }
            }
        }
        System.out.println();
        System.out.println("=== Staged Files ===");
        if (!_staging.isEmpty()) {
            for (String s : _staging.keySet()) {
                System.out.println(s);
            }
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        if (!_remove.isEmpty()) {
            for (String s : _remove) {
                int x = _remove.indexOf(s);
                if (_removebranch.get(x).equals(_currentbranch)) {
                    System.out.println(s);
                }
            }
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        if (!_modified.isEmpty()) {
            for (String s : _modified.keySet()) {
                System.out.println(s + "(" + _modified.get(s) + ")");
            }
        }
        System.out.println();
        System.out.println("=== Untracked Files ===");
        if (!_untracking.isEmpty()) {
            for (String s : _untracking.keySet()) {
                System.out.println(s);
            }
        }
        System.out.println();
    }

    /**
     * Untracking checker.
     **/
    private void untrackchecker() {
        if (!_untracked.isEmpty()) {
            System.out.println("There is an untracked "
                    + "file in the way; "
                    + "delete it or add it first.");
            System.exit(0);
        }
    }

    /**
     * checkout Method. inputs OPERANDS.
     **/
    private void checkout(List<String> operands)
            throws IOException, ClassNotFoundException {
        untrackchecker();
        if (operands.size() == 2) {
            String filename = operands.get(1);
            for (String s : _branch.keySet()) {
                Path dest =
                        Paths.get(".gitlet/commits/" + _branch.get(s));
                Commit commit = (Commit) Utils.deserialize(dest);
                if (commit.getnames().contains(filename)) {
                    filewriter(commit, filename);
                }
            }
        }
        if (operands.size() == 3) {
            String commitid = operands.get(0);
            String filename = operands.get(2);
            String dash = operands.get(1);
            if (!dash.equals("--")) {
                System.out.println("Incorrect Operands");
                System.exit(0);
            }
            for (String s : _commitkeys) {
                int x = commitid.length();
                if (s.substring(0, x).equals(commitid)) {
                    commitid = s;
                }

            }
            if (_commitkeys.contains(commitid)) {
                Path dest = Paths.get(".gitlet/commits/" + commitid);
                Commit commit = (Commit) Utils.deserialize(dest);
                if (!commit.getblobs().keySet().contains(filename)) {
                    System.out.println("File does not exist in that commit.");
                    System.exit(0);
                }
                filewriter(commit, filename);
            } else {
                System.out.println("No commit with that id exists.");
            }
        }

        if (operands.size() == 1) {
            String branch = operands.get(0);
            checkoutchecker(branch);
            checkouthelper(branch);
        }
    }

    /**
     * Checkout error checker input BRANCH.
     **/
    private void checkoutchecker(String branch) {
        if (branch.equals(_currentbranch)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }
        if (!_branch.containsKey(branch)) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
    }

    /**
     * Checkout helper for checkout method input BRANCH.
     **/
    private void checkouthelper(String branch) {
        _files = Utils.plainFilenamesIn(System.getProperty("user.dir"));
        HashMap<String, Blob> temp = new HashMap<>();
        for (String s : _branch.keySet()) {
            if (s.equals(branch)) {
                Path dest = Paths.get(".gitlet/commits/" + _branch.get(s));
                Commit commit = (Commit) Utils.deserialize(dest);
                untrackcheck(commit);
                for (int i = 0; i < commit.getnames().size(); i++) {
                    String name = commit.getnames().get(i);
                    filewriter(commit, name);
                    temp.put(name, commit.getblobs().get(name));
                }
            }
        }
        for (String s : _currentblobs.keySet()) {
            if (!temp.containsKey(s)) {
                File newfile = new File(s);
                newfile.delete();
            }
        }
        _currentblobs.clear();
        _currentblobs.putAll(temp);
        if (branch.equals("master")) {
            _parentid = _branch.get("master");
        } else {
            _parentid = _splitid.get(branch);
        }
        _currentbranch = branch;

        File dir = new File(".gitlet/staging/");
        for (File file : dir.listFiles()) {
            if (!file.isDirectory()) {
                file.delete();
            }
        }
        _staging.clear();
    }

    /**
     * untracker checker input COMMIT.
     **/
    private void untrackcheck(Commit commit) {
        for (String file : _files) {
            if (!_staging.containsKey(file)
                    && !_currentblobs.containsKey(file)
                    && commit.getblobs().containsKey(file)) {
                System.out.println("There is an"
                        + " untracked file in the way; "
                        + "delete it or add it first.");
                System.exit(0);
            }
        }
    }

    /**
     * filewriter input COMMIT, S.
     **/
    private void filewriter(Commit commit, String s) {
        File newfile = new File(s);
        Utils.writeContents(newfile,
                commit.getblobs().get(s).
                        getcontentsasstring());
    }

    /**
     * branch Method, inputs BRANCHNAME.
     **/
    private void branch(String branchname)
            throws IOException, ClassNotFoundException {
        if (_branch.containsKey(branchname)) {
            System.out.println("A branch with that name already exists.");
        } else {
            _branch.put(branchname, _parentid);
            _splitid.put(branchname, _parentid);
        }
    }


    /**
     * RETURNS list from input BRANCH.
     **/
    ArrayList<String> getnamesfrombranch(String branch) {
        ArrayList<String> names = new ArrayList<>();
        for (String s : _branch.keySet()) {
            if (s.equals(branch)) {
                Path dest = Paths.get(".gitlet/commits/" + _branch.get(s));
                Commit commit = (Commit) Utils.deserialize(dest);
                names.addAll(commit.getnames());
            }
        }
        return names;
    }

    /**
     * inputs BRANCH.
     **/
    void delete(String branch) {
        ArrayList<String> chosenbranch = new ArrayList<>();
        chosenbranch.addAll(getnamesfrombranch(branch));

        ArrayList<String> currentbranch = new ArrayList<>();
        currentbranch.addAll(getnamesfrombranch(_currentbranch));

        for (int i = 0; i < currentbranch.size(); i++) {
            if (!chosenbranch.contains(currentbranch.get(i))) {
                File oldfile = new File(chosenbranch.get(i));
                oldfile.delete();
            }
        }
    }

    /**
     * rmbranch Method input BRANCHNAME.
     **/
    private void rmbranch(String branchname)
            throws IOException, ClassNotFoundException {
        if (!_branch.containsKey(branchname)) {
            System.out.println("A branch with that name does not exist.");
        } else if (branchname.equals(_currentbranch)) {
            System.out.println("Cannot remove the current branch.");
        } else {
            _branch.remove(branchname);
        }
    }

    /**
     * reset Method. inputs COMMITID.
     **/
    private void reset(String commitid)
            throws IOException, ClassNotFoundException {
        if (!new File(".gitlet/commits/" + commitid).exists()) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        if (!_untracked.isEmpty()) {
            System.out.println("There is an untracked "
                    + "file in the way; "
                    + "delete it or add it first.");
        }
        List<String> files =
                Utils.plainFilenamesIn(System.getProperty("user.dir"));
        for (String file : files) {
            if (!_staging.containsKey(file)
                    && !_currentblobs.containsKey(file)) {
                System.out.println("There is an untracked "
                        + "file in the way; "
                        + "delete it or add it first.");
                return;
            }
        }
        Commit commit = (Commit)
                Utils.deserialize(Paths.get(".gitlet/commits/"
                        + commitid));
        for (String s : commit.getblobs().keySet()) {
            if (_currentblobs.containsKey(s)
                    || _staging.containsKey(s)) {
                List<String> operands = new ArrayList<>();
                operands.add(commitid);
                operands.add("--");
                operands.add(s);
                checkout(operands);
            }
        }
        _staging.clear();
        _currentbranch = commit.getbranch();
        _branch.put(_currentbranch, commitid);
    }

    /**
     * merger error checkers input BRANCHNAME.
     **/
    private void statements(String branchname) {
        if (!_staging.isEmpty() || _changes > 0) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }
        if (!_branch.containsKey(branchname)) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        if (branchname.equals(_currentbranch)) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }
        if (branchname.equals("master")) {
            _splitpoint = (Commit)
                    Utils.deserialize(Paths.get(".gitlet/commits/"
                            + _splitid.get(_currentbranch)));
        } else {
            _splitpoint = (Commit)
                    Utils.deserialize(Paths.get(".gitlet/commits/"
                            + _splitid.get(branchname)));
        }
    }

    /**
     * merge Method inputs BRANCHNAME.
     **/
    private void merge(String branchname)
            throws IOException, ClassNotFoundException {
        statements(branchname);
        Commit chosenbranchhead =
                (Commit) Utils.deserialize(Paths.get(".gitlet/commits/"
                        + _branch.get(branchname)));
        Commit currentbranchhead =
                (Commit) Utils.deserialize(Paths.get(".gitlet/commits/"
                        + _branch.get(_currentbranch)));
        List<String> files =
                Utils.plainFilenamesIn(System.getProperty("user.dir"));
        if (_splitpoint.gethash().equals(currentbranchhead.gethash())) {
            System.out.println("Current branch fast-forwarded.");
            return;
        }
        if (_splitpoint.gethash().equals(chosenbranchhead.gethash())) {
            System.out.println("Given branch "
                    + "is an ancestor of "
                    + "the current branch.");
            return;
        }
        for (String file : files) {
            if (!_staging.containsKey(file)
                    && !_currentblobs.containsKey(file)
                    && chosenbranchhead.getblobs().containsKey(file)) {
                System.out.println("There is an untracked "
                        + "file in the way; "
                        + "delete it or add it first.");
                return;
            }
        }
        mergehelper1(currentbranchhead, chosenbranchhead);
        mergehelper2(currentbranchhead, chosenbranchhead);
        mergehelper3(currentbranchhead, chosenbranchhead);

        String string = "Merged "
                + branchname
                + " into "
                + _currentbranch + ".";
        Commit commit =
                new Commit(string, _branch.get(_currentbranch),
                        _branch.get(branchname), _currentblobs, _currentbranch);
        _commit.put(commit.gethash(), commit);
        _parentid = commit.gethash();
        _commitkeys.add(_parentid);
        File newfile = new File(".gitlet/commits/" + commit.gethash());
        Utils.writeContents(newfile, Utils.serialize(commit));
        File dir = new File(".gitlet/staging/");
        for (File file : dir.listFiles()) {
            if (!file.isDirectory()) {
                file.delete();
            }
        }
        _branch.put(_currentbranch, commit.gethash());
        if (!_staging.isEmpty()) {
            _staging.clear();
        }
        _remove.clear();
        _removebranch.clear();
    }

    /**
     * mergehelper1 input CURRENTBRANCHHEAD, CHOSENBRANCHHEAD.
     **/
    private void mergehelper1(Commit currentbranchhead,
                              Commit chosenbranchhead)
            throws ClassNotFoundException, IOException {
        for (String s : _splitpoint.getnames()) {
            if (chosenbranchhead.getnames().contains(s)
                    && currentbranchhead.getnames().contains(s)) {
                String splithash =
                        _splitpoint.getblobs().get(s).gethash();
                String chosenhash =
                        chosenbranchhead.getblobs().get(s).gethash();
                String currenthash =
                        currentbranchhead.getblobs().get(s).gethash();
                if (!chosenhash.equals(splithash)
                        && currenthash.equals(splithash)) {
                    File file = new File(s);
                    Utils.writeContents(file,
                            chosenbranchhead.getblobs().
                                    get(s).getcontentsasstring());
                    _staging.put(s,
                            chosenbranchhead.getblobs().get(s));
                    _currentblobs.put(s,
                            chosenbranchhead.getblobs().get(s));
                    List<String> operands = new ArrayList<>();
                    operands.add(chosenbranchhead.gethash());
                    operands.add("---");
                    operands.add(s);
                    checkout(operands);
                }
                if (!chosenhash.equals(splithash)
                        && !currenthash.equals(splithash)) {
                    File path = new File(System.getProperty("user.dir")
                            + File.separator + s);
                    String x = builder(currentbranchhead, chosenbranchhead, s);
                    Utils.writeContents(path,
                            x.getBytes(StandardCharsets.UTF_8));
                    Blob blob = new Blob(s);
                    _staging.put(s, blob);
                    _currentblobs.put(s, blob);
                    System.out.println("Encountered a merge conflict.");
                }
            }
            if (chosenbranchhead.getnames().contains(s)
                    && !_currentblobs.containsKey(s)
                    && !currentbranchhead.getnames().contains(s)) {
                if (!chosenbranchhead.getblobs().get(s).hash()
                        .equals(_splitpoint.getblobs()
                                .get(s).gethash())) {
                    File path = new File(System.getProperty("user.dir")
                            + File.separator + s);
                    String x = builder2(chosenbranchhead, s);
                    Utils.writeContents(path, x);
                    Blob blob = new Blob(s);
                    _staging.put(s, blob);
                    _currentblobs.put(s, blob);
                    System.out.println("Encountered a merge conflict.");
                }
            }
        }
    }

    /**
     * string builder input CURRENTBRANCHHEAD, CHOSENBRANCHHEAD, S
     * return STRING.
     **/
    private String builder(Commit currentbranchhead,
                           Commit chosenbranchhead, String s) {
        String newstring = "<<<<<<< HEAD"
                + System.getProperty("line.separator")
                + currentbranchhead.getblobs().
                get(s).getcontentsasstring()
                + "======="
                + System.getProperty("line.separator")
                + chosenbranchhead.getblobs().
                get(s).getcontentsasstring()
                + ">>>>>>>"
                + System.getProperty("line.separator");
        return newstring;
    }

    /**
     * string builder input COMMIT, S,
     * return STRING.
     **/
    private String builder2(Commit commit, String s) {
        String x = "<<<<<<< HEAD"
                + System.lineSeparator()
                + commit.getblobs()
                .get(s).getcontentsasstring()
                + "======="
                + System.lineSeparator()
                + ">>>>>>>"
                + System.lineSeparator();
        return x;
    }

    /**
     * merge helper input CURRENTBRANCHHEAD, CHOSENBRANCHHEAD.
     **/
    private void mergehelper2(Commit currentbranchhead,
                              Commit chosenbranchhead)
            throws IOException, ClassNotFoundException {
        for (String s : chosenbranchhead.getnames()) {
            if (!_splitpoint.getnames().contains(s)
                    && !currentbranchhead.getnames().contains(s)) {
                List<String> operands = new ArrayList<>();
                operands.add(chosenbranchhead.gethash());
                operands.add("--");
                operands.add(s);
                checkout(operands);
                _currentblobs.put(s, chosenbranchhead.getblobs().get(s));
                _staging.put(s, chosenbranchhead.getblobs().get(s));
            }
            if (!_splitpoint.getnames().contains(s)
                    && currentbranchhead.getnames().contains(s)) {
                if (!chosenbranchhead.getblobs().get(s).gethash().
                        equals(currentbranchhead.getblobs().get(s).gethash())) {
                    File path = new File(System.getProperty("user.dir")
                            + File.separator + s);
                    String x = builder(currentbranchhead, chosenbranchhead, s);
                    Utils.writeContents(path, x);
                    Blob blob = new Blob(s);
                    _staging.put(s, blob);
                    _currentblobs.put(s, blob);
                    System.out.println("Encountered a merge conflict.");
                }
            }
        }
    }

    /**
     * merge helper input CURRENTBRANCHHEAD, CHOSENBRANCHHEAD.
     **/
    private void mergehelper3(Commit currentbranchhead, Commit chosenbranchhead)
            throws IOException, ClassNotFoundException {
        for (String current : currentbranchhead.getnames()) {
            if (!_splitpoint.getnames().contains(current)
                    && !chosenbranchhead.getnames().contains(current)) {
                List<String> operands = new ArrayList<>();
                operands.add(currentbranchhead.gethash());
                operands.add("--");
                operands.add(current);
                checkout(operands);
                _currentblobs.put(current,
                        currentbranchhead.getblobs().get(current));
                _staging.put(current,
                        currentbranchhead.getblobs().get(current));
            }
            if (_splitpoint.getnames().contains(current)
                    && !chosenbranchhead.getnames().contains(current)) {
                if (currentbranchhead.getblobs().get(current).gethash().
                        equals(_splitpoint.getblobs().get(current).hash())) {
                    rm(current);
                } else {
                    File path = new File(System.getProperty("user.dir")
                            + File.separator + current);
                    String x = builder2(currentbranchhead, current);
                    Utils.writeContents(path, x);
                    Blob blob = new Blob(current);
                    _staging.put(current, blob);
                    _currentblobs.put(current, blob);
                    System.out.println("Encountered a merge conflict.");

                }
            }
            if (!_splitpoint.getnames().contains(current)
                    && chosenbranchhead.getnames().contains(current)) {
                if (!chosenbranchhead.getblobs().get(current).gethash().
                        equals(currentbranchhead.getblobs().
                                get(current).gethash())) {
                    File path = new File(System.getProperty("user.dir")
                            + File.separator + current);
                    String x =
                            builder(currentbranchhead,
                                    chosenbranchhead, current);
                    Utils.writeContents(path, x);
                    Blob blob = new Blob(current);
                    _staging.put(current, blob);
                    _currentblobs.put(current, blob);
                    System.out.println("Encountered a merge conflict.");
                }
            }
        }
    }


    /**
     * checks untracked.
     **/
    private void checkuntrackedmodified() {
        List<String> files =
                Utils.plainFilenamesIn(System.getProperty("user.dir"));
        if (!_commitkeys.isEmpty()) {
            _checkingcommit = (Commit)
                    Utils.deserialize(Paths.get(".gitlet/commits/"
                            + _parentid));
        }
        for (String file : files) {
            if (!_staging.containsKey(file)
                    && !_currentblobs.containsKey(file)
                    && !_removedbuttracked.contains(file)) {
                _untracking.put(file, new Blob(file));
            }
            if (_checkingcommit != null
                    && _checkingcommit.getblobs().containsKey(file)) {
                if (!_staging.containsKey(file)
                        && _checkingcommit.getnames().contains(file)) {
                    if (!new Blob(file).
                            gethash().equals(_checkingcommit.getblobs().
                            get(file).gethash())) {
                        _modified.put(file, "modified");
                    }
                }
            }
        }
        for (String s : _modified.keySet()) {
            if (!new File(s).exists()) {
                _modified.put(s, "deleted");
            }
        }
    }

}
