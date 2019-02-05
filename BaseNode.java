//Atharva Phulambrikar
//amp170230

package Tickets;

abstract class BaseNode {
    //Members
    int row;
    char seat;
    boolean reserved;
    char ticketType;
    //Methods
    //Overloaded constructor
    BaseNode(int r, char s, boolean res, char tT)
    {
        this.row = r;
        this.seat = s;
        this.reserved = res;
        this.ticketType = tT;
    }
    //Accessors
    int getRow()
    {
        return this.row;
    }
    char getSeat()
    {
        return this.seat;
    }
    boolean getReserved()
    {
        return this.reserved;
    }
    char getTicketType()
    {
        return this.ticketType;
    }
    //Mutators
    void setRow(int r)
    {
        this.row = r;
    }
    void setSeat(char s)
    {
        this.seat = s;
    }
    void setReserved(boolean r)
    {
        this.reserved = r;
    }
    void setTicketType(char t)
    {
        this.ticketType = t;
    }
}
