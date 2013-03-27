package edu.lastcow.hids.db.model;

/**
 * Created with IntelliJ IDEA.
 * User: lastcow
 * Date: 3/13/13
 * Time: 8:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class WhiteList {

    private long id;
    private String whiteName;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWhiteName() {
        return whiteName;
    }

    public void setWhite_name(String whiteName) {
        this.whiteName = whiteName;
    }

    @Override
    public String toString(){
        return whiteName;
    }
}
