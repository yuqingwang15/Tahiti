package octoteam.tahiti.server.model;

import com.google.common.base.MoreObjects;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@DatabaseTable(tableName = "accounts")
public class Account {

    /**
     * TODO
     */
    @DatabaseField(generatedId = true)
    private int id;

    /**
     * TODO
     */
    @DatabaseField(canBeNull = false)
    private String username;

    /**
     * TODO
     */
    @DatabaseField(canBeNull = false)
    private String hash;

    /**
     * TODO
     */
    @DatabaseField(canBeNull = false)
    private String salt;

    public Account() {

    }

    public Account(String username, String password) {
        this.username = username;
        this.salt = RandomStringUtils.random(32);
        this.hash = hashPassword(password, salt);
    }

    public void assignId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    /**
     * TODO
     *
     * @param password
     * @return
     */
    public boolean isPasswordMatches(String password) {
        return hashPassword(password, salt).equals(hash);
    }

    /**
     * TODO
     *
     * @param password
     * @param salt
     * @return
     */
    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update((password + salt).getBytes("UTF-8"));
            byte[] digest = md.digest();
            return String.format("%064x", new java.math.BigInteger(1, digest));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("username", username)
                .add("hash", hash)
                .add("salt", salt)
                .toString();
    }
}
