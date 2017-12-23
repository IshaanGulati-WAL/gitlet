package gitlet;

import ucb.junit.textui;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.nio.file.Paths;

/** The suite of all JUnit tests for the gitlet package.
 *  @author Tosh
 */
public class UnitTest {

    /**
     * Run the JUnit tests in the loa package. Add xxxTest.class entries to
     * the arguments of runClasses to run other JUnit tests.
     */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTest.class);
    }

    /**
     * A dummy test to avoid complaint.
     */
    @Test
    public void initialtest() {
        int count = 0;
        for (int k = 1; k < 20; k*=2){
            for (int j = 0; j < k; j += 1){
                count++;
            }
        }
        System.out.println(count);
    }

    @Test
    public void hashinitialcheck() {
        String message = "initial commit";
        String branch = "master";
        String message2 = "initial commit";
        String branch2 = "master";
        Commit newcommit = new Commit(message, branch);
        Commit secondcommit = new Commit(message2, branch2);
        assertNotEquals(newcommit, secondcommit);
        assertEquals("master", newcommit.getbranch());
        assertEquals(null, newcommit.getparentid());
        assertEquals(newcommit.gethash(), secondcommit.gethash());
    }

    @Test
    public void initialblobcheck() throws IOException {
        StringBuilder string = new StringBuilder();
        string.append("this is a wug");
        String result = string.toString();
        File file = new File("check");
        file.createNewFile();
        Utils.writeContents(file, result);
        Blob blob = new Blob(file.getName());
        assertEquals("this is a wug", blob.getcontentsasstring());
        assertEquals("check", blob.getname());
    }

    @Test
    public void compareblobs() throws IOException {
        StringBuilder string1 = new StringBuilder();
        string1.append("this is a wug");
        String result1 = string1.toString();

        StringBuilder string2 = new StringBuilder();
        string2.append("this is not a wug");
        String result2 = string2.toString();

        File firstfile = new File("firstfile");
        firstfile.createNewFile();
        Utils.writeContents(firstfile, result1);
        Blob blob = new Blob(firstfile.getName());

        File secondfile = new File("secondfile");
        secondfile.createNewFile();
        Utils.writeContents(secondfile, result2);
        Blob secondblob = new Blob(secondfile.getName());

        assertEquals("this is a wug", blob.getcontentsasstring());
        assertEquals("this is not a wug", secondblob.getcontentsasstring());
        assertEquals("firstfile", blob.getname());
        assertEquals("secondfile", secondblob.getname());
        assertNotEquals(blob.gethash(), secondblob.gethash());
    }

    @Test
    public void detailedcommit() throws IOException {
        StringBuilder string1 = new StringBuilder();
        string1.append("this is a wug");
        String result1 = string1.toString();
        StringBuilder string2 = new StringBuilder();
        string2.append("this is not a wug");
        String result2 = string2.toString();
        File firstfile = new File("firstfile");
        firstfile.createNewFile();
        Utils.writeContents(firstfile, result1);
        Blob blob = new Blob(firstfile.getName());
        File secondfile = new File("secondfile");
        secondfile.createNewFile();
        Utils.writeContents(secondfile, result2);
        Blob secondblob = new Blob(secondfile.getName());
        LinkedHashMap<String, Blob> first = new LinkedHashMap<>();
        first.put(blob.getname(), blob);
        LinkedHashMap<String, Blob> second = new LinkedHashMap<>();
        second.put(blob.getname(), blob);
        second.put(secondblob.getname(), blob);
        LinkedHashMap<String, Blob> third = new LinkedHashMap<>();
        third.put(blob.getname(), blob);
        third.put(secondblob.getname(), blob);
        String message = "first commit";
        String branch = "master";
        String message2 = "second commit";
        String branch2 = "master";
        String message3 = "third commit";
        String branch3 = "branch";
        String message4 = "second commit";
        String branch4 = "master";
        Commit firstcommit = new Commit(message, "2017", first, branch);
        Commit secondcommit = new Commit(message2, "2015", second, branch2);
        Commit thirdcommit = new Commit(message3, "2017", second, branch3);
        Commit fourthcommit = new Commit(message4, "2015", second, branch4);
        assertEquals("first commit", firstcommit.getmessage());
        assertEquals("second commit", secondcommit.getmessage());
        assertEquals("third commit", thirdcommit.getmessage());
        assertEquals(1, firstcommit.getblobs().size());
        assertEquals(2, secondcommit.getblobs().size());
        assertEquals(2, secondcommit.getblobs().size());
        assertEquals(secondcommit.getblobs(), thirdcommit.getblobs());
        assertEquals(firstcommit.getbranch(), secondcommit.getbranch());
        assertNotEquals(firstcommit.getbranch(), thirdcommit.getbranch());
        assertEquals(secondcommit.gethash(), fourthcommit.gethash());
        writefiles(firstcommit, secondcommit, thirdcommit, fourthcommit);
        firstcommit = (Commit) Utils.deserialize(Paths.get("first"));
        secondcommit = (Commit) Utils.deserialize(Paths.get("second"));
        thirdcommit = (Commit) Utils.deserialize(Paths.get("third"));
        fourthcommit = (Commit) Utils.deserialize(Paths.get("fourth"));
        assertEquals("first commit", firstcommit.getmessage());
        assertEquals("second commit", secondcommit.getmessage());
        assertEquals("third commit", thirdcommit.getmessage());
        assertEquals(1, firstcommit.getblobs().size());
        assertEquals(2, secondcommit.getblobs().size());
        assertEquals(2, secondcommit.getblobs().size());
        assertEquals(firstcommit.getbranch(), secondcommit.getbranch());
        assertNotEquals(firstcommit.getbranch(), thirdcommit.getbranch());
        assertEquals(secondcommit.gethash(), fourthcommit.gethash());
    }
    private void writefiles(Commit firstcommit,
                            Commit secondcommit,
                            Commit thirdcommit,
                            Commit fourthcommit) {
        File firstfile = new File("first");
        File secondfile = new File("second");
        File thirdfile = new File("third");
        File fourthfile = new File("fourth");
        Utils.writeContents(firstfile, Utils.serialize(firstcommit));
        Utils.writeContents(secondfile, Utils.serialize(secondcommit));
        Utils.writeContents(thirdfile, Utils.serialize(thirdcommit));
        Utils.writeContents(fourthfile, Utils.serialize(fourthcommit));
    }
}


