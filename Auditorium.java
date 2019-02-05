//Atharva Phulambrikar
//amp170230

package Tickets;

import java.io.*;
import java.text.DecimalFormat;

public class Auditorium {
    //Members
    TheaterSeat first;
    TheaterSeat rowI;
    TheaterSeat colI;
    static int rows = 0, columns = 0, totalqty = 0, bestRowNo = 0; //Variables for the auditorium
    //Constructor
    Auditorium(int rowsNum, int columnsNum, BufferedReader br) throws IOException   //Auditorium constructor
    {
        rows = rowsNum;
        columns = columnsNum;
        char ch;
        boolean bool;
        ch = (char)br.read();
        br.mark(260);
        if (ch == 'A' || ch == 'C' || ch == 'S')
        {
            bool = true;
            first = new TheaterSeat(0, 'A', bool, ch);
        }
        else
        {
            bool = false;
            first = new TheaterSeat(0, 'A', bool, '.');
        }
        rowI = colI = first;    //Head of grid
        br.reset();
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                if (i == 0)
                {
                    if (j < columns - 1)
                    {
                        ch = (char)br.read();
                        if (ch == 'A' || ch == 'C' || ch == 'S')
                            bool = true;
                        else
                            bool = false;
                        rowI.right = new TheaterSeat(i, (char)(j + 65 + 1), bool, ch);
                        rowI.right.left = rowI;
                        rowI = rowI.right;
                    }
                }
                else
                {
                    if (j < columns - 1)
                    {
                        if (j == 0)
                        {
                            rowI.up = colI;
                        }
                        ch = (char)br.read();
                        if (ch == 'A' || ch == 'C' || ch == 'S')
                            bool = true;
                        else
                            bool = false;
                        rowI.right = new TheaterSeat(i, (char)(j + 65 + 1), bool, ch);
                        rowI.up.down = rowI;
                        rowI.right.left = rowI;
                        rowI.right.up = rowI.up.right;
                        rowI = rowI.right;
                    }
                }
            }
            if (i < rows - 1)
            {
                br.readLine();
                ch = (char)br.read();
                if (ch == 'A' || ch == 'C' || ch == 'S')
                    bool = true;
                else
                    bool = false;
                colI.down = new TheaterSeat(i + 1, 'A', bool, ch);
                rowI = colI = colI.down;
            }
        }   //Read characters into grid
    }
    void setTheaterSeat(int r, int c, char tT)
    {
        rowI = first;
        for (int i = 0; i < r; i++)
            rowI = rowI.down;
        for (int j = 0; j < c; j++)
            rowI = rowI.right;
        rowI.setTicketType(tT);
        if (tT == 'A' || tT == 'C' || tT == 'S')
            rowI.setReserved(true);
        else
            rowI.setReserved(false);
    }
    TheaterSeat getTheaterSeat(int r, int c)
    {
        rowI = first;
        for (int i = 0; i < r; i++)
            rowI = rowI.down;
        for (int j = 0; j < c; j++)
            rowI = rowI.right;
        return rowI;
    }
    void displayAuditorium(int r, int c)
    {
        rowI = colI = first;
        for (int i = 0; i < r; i++)
        {
            if (i == 0)
            {
                System.out.print(" ");  //Initial space in top left corner
                int k = 65;
                do
                {
                    System.out.print((char)k);
                    k++;
                }while(k - 65 < columns);
                System.out.println();   //Column letters printed, move to next line
            }
            for (int j = 0; j < c; j++)
            {
                if (j == 0)
                {
                    System.out.print(i + 1);
                }
                if (rowI.ticketType == 'A' || rowI.ticketType == 'S' || rowI.ticketType == 'C' )
                    System.out.print('#');
                else
                    System.out.print('.');
                rowI = rowI.right;
            }
            rowI = colI.down;
            colI = colI.down;
            System.out.println();
        }
    }
    boolean checkAvailability(int rn, int ss, int tqty)
    {
        if (ss + tqty - 1 > columns)
            return false;
        for (int i = 0; i < tqty; i++)
        {
            if (getTheaterSeat(rn - 1, ss - 1 + i).getTicketType() != '.')  //If it is occupied, return false
                return false;
        }
        return true;
    }
    void reserveSeats(int rn, int ss, int a, int c, int s)
    {
        for (int i = 0; i < a; i++)
        {
            setTheaterSeat(rn - 1, ss - 1 + i, 'A');    //Adult
        }
        for (int i = 0; i < c; i++)
        {
            setTheaterSeat(rn - 1, ss - 1 + i + a, 'C');    //Child
        }
        for (int i = 0; i < s; i++)
        {
            setTheaterSeat(rn - 1, ss - 1 + i + a + c, 'S');    //Senior
        }
    }
    TheaterSeat bestAvailable(int tqty)
    {
        double midr, midc;
        midr = ((double)(rows + 1)/2);
        midc = ((double)(columns - 1)/2);
        TheaterSeat rowIt = first;
        int minRow = -1;
        int minCol = -1;
        double minD = Double.MAX_VALUE;
        boolean b = false;
        double minDist = Double.MAX_VALUE;
        int seatNo;
        for (int i = 0; i < rows; i++)
        {
            seatNo = bestRow(i + 1, tqty);  //Best seat in each row
            if (seatNo != -1)
                minD = (double)(midr - (i + 1))*(midr - (i + 1)) + (double)((midc - (double)(seatNo + tqty - 1 + seatNo)/2)*(midc - (double)(seatNo + tqty - 1 + seatNo)/2));   //Calculate minimum distance
            if (minD < minDist) //If distance is lesser than previous minDist
            {
                minDist = minD;
                b = true;
                minRow = i;
                minCol = seatNo;
            }
            else if (minD == minDist)   //SPECIAL CASE if distance is same, then take row with minimum row number
            {
                if (Math.abs(minRow + 1 - midr) > Math.abs(i + 1 - midr))
                {
                    minDist = minD;
                    b = true;
                    minRow = i;
                    minCol = seatNo;
                }
            }
            rowIt = rowIt.down;
            minD = Double.MAX_VALUE;
            seatNo = -1;
        }
        if (b == true && minRow != rows - 1)
            return getTheaterSeat(minRow + 1, minCol);
        else if (b == true && minRow == rows - 1)
            return new TheaterSeat(rows, (char)(minCol + 65), false, '.');
        return new TheaterSeat(-1, 'A', false, '.');    //Return a seat with row number -1 if no seat found
    }
    int bestRow(int r, int tqty)    //Calculates best seats in one row
    {
        boolean occ = false;    //Boolean to check occupancy
        double sdist = 0, shortest = columns; //Shortest dist to -1 since abs value of distance will never be < 0
        int starts = -1;    //Starting column index number
        for (int i = 0; i < columns - tqty + 1; i++)
        {
            occ = false;
            for (int j = 0; j < tqty; j++)
            {
                if (getTheaterSeat(r - 1, i + j).getTicketType() != '.')
                {
                    occ = true; //If occupied
                    break;
                }
            }
            if (occ == false)   //If not occupied
            {
                sdist = Math.abs(((float)i + (float)(tqty - 1)/2) - (float)(columns)/2);   //Shortest distance from the middle seat, using floats for exact precision
                if (sdist < shortest)
                {
                    shortest = sdist;
                    starts = i;
                }//Shortest distance and shortest seat column no
            }
        }
        if (starts != -1)
            return starts;  //Add 1 because loop starts from 0
        else
            return starts;  //Negative one if not found
    }
    void displayReport(Auditorium theatre)
    {
        int totalqty = 0;
        int noAdult = 0;
        int noChild = 0;
        int noSenior = 0;
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                if (theatre.getTheaterSeat(i,j).getTicketType() == 'A')
                    noAdult++;
                else if (theatre.getTheaterSeat(i,j).getTicketType() == 'C')
                    noChild++;
                else if (theatre.getTheaterSeat(i,j).getTicketType() == 'S')
                    noSenior++;
                totalqty++; //Increment total quantity each time
            }
        }
        double totalSold = (noAdult*10) + (noChild*5) + (noSenior*7.5); //Prices multiplied with each individual element
        DecimalFormat df = new DecimalFormat(".00");    //Decimal format to round up to 2 decimal places
        System.out.printf("%-30s %-30s\n", "Total seats in Auditorium :", totalqty);
        System.out.printf("%-30s %-30s\n", "Total tickets sold :", (noAdult + noChild + noSenior));
        System.out.printf("%-30s %-30s\n", "Adult tickets sold :", noAdult);
        System.out.printf("%-30s %-30s\n", "Child tickets sold :", noChild);
        System.out.printf("%-30s %-30s\n", "Senior tickets sold :", noSenior);
        System.out.printf("%-30s", "Total ticket sales :");
        System.out.printf(" $");
        System.out.printf("%-30s\n", df.format(totalSold));
    }
}