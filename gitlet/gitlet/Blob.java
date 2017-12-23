package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/** Blob class containing information about a file.
 * @author Tosh **/

public class Blob implements Serializable {
    /**Name of file. **/
    private String _name;
    /** Hash of file. **/
    private String _hashCode;
    /** Contents as a byte array. **/
    private byte[] _contents;
    /** Contents as a string. **/
    private String _contentsasstring;

    /** Blob constructor inputs NAME. **/
    public Blob(String name) {
        File file = new File(name);
        _name = name;
        _contents = Utils.readContents(file);
        _contentsasstring = Utils.readContentsAsString(file);
        _hashCode = hash();
    }
    /** Returns name. **/
    public String getname() {
        return _name;
    }
    /** Returns hash. **/
    public String gethash() {
        return _hashCode;
    }

    /** Returns hash generated. **/
    public String hash() {
        List<Object> contents = new ArrayList<>();
        contents.add(_name);
        contents.add(_contents);
        contents.add(_contentsasstring);
        return Utils.sha1(contents);
    }

    /** Returns getcontents. **/
    public byte[] getcontents() {
        return _contents;
    }

    /** Returns getcontentsasstring. **/
    public String getcontentsasstring() {
        return _contentsasstring;
    }
}
