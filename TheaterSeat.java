//Atharva Phulambrikar
//amp170230

package Tickets;

public class TheaterSeat extends BaseNode
{
    //Members
    TheaterSeat up;
    TheaterSeat down;
    TheaterSeat left;
    TheaterSeat right;
    //Methods
    //Overloaded Constructor
    TheaterSeat(int r, char s, boolean res, char tT)
    {
        super(r,s,res,tT);
    }
    //Accessors
    TheaterSeat getUp()
    {
        return this.up;
    }
    TheaterSeat getDown()
    {
        return this.down;
    }
    TheaterSeat getLeft()
    {
        return this.left;
    }
    TheaterSeat getRight()
    {
        return this.right;
    }
    //Mutators
    void setUp(TheaterSeat u)
    {
        this.up = u;
    }
    void setDown(TheaterSeat d)
    {
        this.down = d;
    }
    void setLeft(TheaterSeat l)
    {
        this.left = l;
    }
    void setRight(TheaterSeat r)
    {
        this.right = r;
    }
}
