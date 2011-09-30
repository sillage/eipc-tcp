package kernel;

import java.util.Date;

import common.Segment;

/**
 * @author Sebastien Gislais
 *
 * This class is an equivalence of the c++ class "pair"
 */
public class Pair {

    private Date _creationDate = null;
    private Segment _segment = null;

    public Pair(Date creationDate, Segment segment) {
        _creationDate = creationDate;
        _segment = segment;

    }

    public Pair(Segment segment) {
        _segment = segment;
        _creationDate = new Date();
    }

    public Date getCreationDate() {
        return _creationDate;
    }

    public void setCreationDate(Date creationDate) {
        _creationDate = creationDate;
    }

    public Segment getSegment() {
        return _segment;
    }

    public void setSegment(Segment segment) {
        _segment = segment;
    }
}
