//Atharva Phulambrikar
//amp170230

package Tickets;

import java.util.*;
import java.io.*;
import java.text.DecimalFormat;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        File file = new File("userdb.dat");
        if (!file.exists())
            System.exit(0);
        HashMap<String, UserOrder> map = new HashMap<>();
        if (file.exists())
        {  
            Scanner sc = new Scanner(file);
            String s;
            String [] a;
            UserOrder temp;
            while (sc.hasNextLine())
            {
                temp = new UserOrder();
                s = sc.nextLine();
                a = s.split(" ");
                temp.username = a[0];
                temp.password = a[1];
                map.put(temp.username, temp);
            }
            int count = 0;
            boolean isAdmin = false;
            sc.close();
            sc = new Scanner(System.in);
            do
            {
                count = 0;
                System.out.println("Please enter your username.");
                s = sc.nextLine();
                temp = map.get(s);
                do
                {
                    System.out.println("Please enter your password.");
                    s = sc.nextLine();
                    if (temp.password.equals(s) && temp.username.equals("admin"))
                    {
                        isAdmin = true;
                        break;
                    }
                    else if (temp.password.equals(s))
                        break;
                    else
                    {
                        count++;
                        System.out.println("Incorrect password.");
                    }
                }while(count < 3);
                if (count == 3)
                {
                    System.out.println("You've entered the password incorrectly 3 times.");
                    continue;
                }
                if (isAdmin == true)
                {
                    int adminChoice = 0;
                    do
                    {
                        adminChoice = 0;
                        System.out.println("1. Print Report");
                        System.out.println("2. Logout");
                        System.out.println("3. Exit");
                        do
                        {
                            do
                            {
                                try
                                {
                                    String str = sc.nextLine();
                                    adminChoice = Integer.parseInt(str);
                                    break;
                                }
                                catch(NumberFormatException ex)
                                {
                                    System.out.println("Invalid input. Please enter a number between 1-3.");
                                }
                            }while(true);
                            if (adminChoice > 0 && adminChoice <= 3)
                                break;
                            else
                                System.out.println("Invalid input. Please enter a number between 1-3.");
                        }while(true);
                        if (adminChoice == 1)
                        {
                            int audiNo = 0, numOpen = 0, numOpenTot = 0, totReserved = 0, totReservedTot = 0, numAdult = 0, numAdultTot = 0, numChild = 0, numChildTot = 0, numSenior = 0, numSeniorTot = 0;
                            double sales = 0, salesTot = 0;
                            audiNo = 1;
                            int rows = 0, columns = 0;
                            System.out.printf("%-30s %-30s %-30s %-30s %-30s %-30s %-30s\n", "Labels", "Open Seats", "Total Reserved Seats", "Adult Seats", "Child Seats", "Senior Seats", "Ticket Sales");
                            DecimalFormat df = new DecimalFormat(".00");    //Decimal format to round up to 2 decimal places
                            for(int c = 0; c < 3; c++)
                            {
                                File audi = new File("A" + audiNo + ".txt");
                                if (!audi.exists())
                                    System.exit(0);
                                Scanner col = new Scanner(audi);
                                BufferedReader br = new BufferedReader(new FileReader(audi));
                                br.mark(260);   //Mark at 260 because max no. of rows x max no. of columns = 26 x 10 = 260
                                while(br.readLine() != null)
                                {
                                    rows++;
                                }
                                if (rows == 0)
                                {
                                    System.out.println("File empty");
                                    System.exit(0);
                                }
                                br.reset();
                                if ((br.readLine().length() == 1) && (rows == 1))   //Special Case if only ONE Seat
                                    columns++;
                                else
                                {
                                    while (col.hasNext())
                                    {
                                        columns = col.nextLine().length();
                                    }
                                }
                                br.reset(); //Reset BufferedReader pointer
                                Auditorium theatre = new Auditorium(rows, columns, br);
                                sales = 0;
                                numOpen = totReserved = numAdult = numChild = numSenior = 0;
                                for (int i = 0; i < rows; i++)
                                {
                                    for (int j = 0; j < columns; j++)
                                    {
                                        if (theatre.getTheaterSeat(i,j).getTicketType() == 'A')
                                        {
                                            numAdult++;
                                            numAdultTot++;
                                            sales += 10;
                                            salesTot += 10;
                                        }
                                        else if (theatre.getTheaterSeat(i,j).getTicketType() == 'C')
                                        {
                                            numChild++;
                                            numChildTot++;
                                            sales += 5;
                                            salesTot += 5;
                                        }
                                        else if (theatre.getTheaterSeat(i,j).getTicketType() == 'S')
                                        {
                                            numSenior++;
                                            numSeniorTot++;
                                            sales += 7.5;
                                            salesTot += 7.5;
                                        }
                                        else
                                        {
                                            numOpen++;
                                            numOpenTot++;
                                            continue;
                                        }
                                        totReserved++; //Increment total quantity each time
                                        totReservedTot++;
                                    }
                                }
                                System.out.printf("%-30s %-30s %-30s %-30s %-30s %-30s %-30s\n", "Auditorium " + audiNo, numOpen, totReserved, numAdult, numChild, numSenior, "$" + df.format(sales));
                                audiNo++;
                            }
                            System.out.printf("%-30s %-30s %-30s %-30s %-30s %-30s %-30s\n", "Total", numOpenTot, totReservedTot, numAdultTot, numChildTot, numSeniorTot, "$" + df.format(salesTot));
                        }
                        else if (adminChoice == 3)
                        {
                            System.exit(0);
                        }
                    }while(adminChoice != 2);
                    if (adminChoice == 2)
                    {
                        isAdmin = false;
                        continue;
                    }
                }
                int choice = 0;
                do
                {
                    int rows = 0, columns = 0, totalqty = 0; //Variables for the auditorium
                    int rowNo = 0, noAdult = 0, noChild = 0, noSenior = 0;   //Variables for the various numbers
                    char startSeat, bestAns; //Character variables
                    TheaterSeat bestSeat;    //BestSeat variable
                    int audichoice = 0;
                    System.out.println("1. Reserve Seats");
                    System.out.println("2. View Orders");
                    System.out.println("3. Update Order");
                    System.out.println("4. Display Receipt");
                    System.out.println("5. Log Out");
                    do
                    {
                        do
                        {
                            try
                            {
                                String str = sc.nextLine();
                                choice = Integer.parseInt(str);
                                break;
                            }
                            catch(NumberFormatException ex)
                            {
                                System.out.println("Invalid input. Please enter a number between 1-5.");
                            }
                        }while(true);
                        if (choice > 0 && choice <= 5)
                            break;
                        else
                            System.out.println("Invalid input. Please enter a number between 1-5.");
                    }while(true);
                    if (choice < 1 || choice > 5)
                        continue;
                    if (choice == 1)
                    {
                        do
                        {
                            System.out.println("1. Auditorium 1");
                            System.out.println("2. Auditorium 2");
                            System.out.println("3. Auditorium 3");
                            try
                            {
                                s = sc.nextLine();
                                audichoice = Integer.parseInt(s);
                                break;
                            }
                            catch(NumberFormatException ex)
                            {
                                System.out.println("Invalid input. Please enter either 1, 2 or 3.");
                            }
                        }while(true);
                        File audi = new File("A" + audichoice + ".txt");
                        if (!audi.exists())
                            System.exit(0);
                        Scanner col = new Scanner(audi);
                        BufferedReader br = new BufferedReader(new FileReader(audi));
                        br.mark(260);   //Mark at 260 because max no. of rows x max no. of columns = 26 x 10 = 260
                        while(br.readLine() != null)
                        {
                            rows++;
                        }
                        if (rows == 0)
                        {
                            System.out.println("File empty");
                            System.exit(0);
                        }
                        br.reset();
                        if ((br.readLine().length() == 1) && (rows == 1))   //Special Case if only ONE Seat
                            columns++;
                        else
                        {
                            while (col.hasNext())
                            {
                                columns = col.nextLine().length();
                            }
                        }
                        br.reset(); //Reset BufferedReader pointer
                        Auditorium theatre = new Auditorium(rows, columns, br);
                        theatre.displayAuditorium(rows, columns);
                        System.out.println("Enter row number");
                        do
                        {
                            while(true)
                            {
                                try
                                {
                                    String str = sc.nextLine();
                                    rowNo = Integer.parseInt(str);
                                    break;
                                }
                                catch(NumberFormatException ex)
                                {
                                    System.out.println("Invalid input. Please enter a valid row number.");
                                }
                            }
                            if (rowNo > 0 && rowNo <= rows)
                                break;
                            else
                                System.out.println("Invalid input. Please enter a valid row number.");
                        }while(true);
                        System.out.println("Enter starting seat letter");
                        do
                        {
                            while(true)
                            {
                                try
                                {
                                    String str = sc.nextLine();
                                    if (str.length() == 1)
                                    {
                                        startSeat = Character.toUpperCase(str.charAt(0));
                                        break;
                                    }
                                    else
                                    {
                                        startSeat = '0';
                                        break;
                                    }
                                }
                                catch(StringIndexOutOfBoundsException ex)
                                {
                                    System.out.println("Invalid input. Please enter a valid starting seat letter.");
                                }
                            }
                            if ((int)startSeat - 64 > 0 && (int)startSeat - 64 <= columns)
                                break;
                            else
                                System.out.println("Invalid input. Please enter a valid starting seat letter.");
                        }while(true);
                        System.out.println("Enter number of adult tickets");
                        do
                        {
                            while(true)
                            {
                                try
                                {
                                    String str = sc.nextLine();
                                    noAdult = Integer.parseInt(str);
                                    break;
                                }
                                catch(NumberFormatException ex)
                                {
                                    System.out.println("Invalid input. Please enter a valid number of tickets.");
                                }
                            }
                            if (noAdult >= 0)
                                break;
                            else
                                System.out.println("Invalid input. Please enter a valid number of tickets.");
                        }while(true);
                        System.out.println("Enter number of child tickets");
                        do
                        {
                            while(true)
                            {
                                try
                                {
                                    String str = sc.nextLine();
                                    noChild = Integer.parseInt(str);
                                    break;
                                }
                                catch(NumberFormatException ex)
                                {
                                    System.out.println("Invalid input. Please enter a valid number of tickets.");
                                }
                            }
                            if (noChild >= 0)
                                break;
                            else
                                System.out.println("Invalid input. Please enter a valid number of tickets.");
                        }while(true);
                        System.out.println("Enter number of senior tickets");
                        do
                        {
                            while(true)
                            {
                                try
                                {
                                    String str = sc.nextLine();
                                    noSenior = Integer.parseInt(str);
                                    break;
                                }
                                catch(NumberFormatException ex)
                                {
                                    System.out.println("Invalid input. Please enter a valid number of tickets.");
                                }
                            }
                            if (noSenior >= 0)
                                break;
                            else
                                System.out.println("Invalid input. Please enter a valid number of tickets.");
                        }while(true);
                        totalqty = noAdult + noChild + noSenior;
                        int seatStart = (int)startSeat - 64;
                        if(theatre.checkAvailability(rowNo, seatStart, totalqty))
                        {
                            theatre.reserveSeats(rowNo, seatStart, noAdult, noChild, noSenior);
                            if (totalqty > 0)
                                System.out.println("Great! Your seats have been reserved.");
                            PrintWriter output = new PrintWriter(audi); //To write back to original file
                            for (int i = 0; i < rows; i++)
                            {
                                for (int j = 0; j < columns; j++)
                                {
                                    output.print(theatre.getTheaterSeat(i, j).ticketType);
                                }
                                output.println();
                            }
                            output.flush();
                            output.close();
                            temp.ord.add(new Orders(audichoice, rowNo, seatStart, noAdult, noChild, noSenior));
                        }
                        else
                        {
                            bestSeat = theatre.bestAvailable(totalqty);
                            if(bestSeat.getRow() != -1 && bestSeat.getSeat() >= 'A' && bestSeat.getSeat() <= 'Z')
                            {
                                if (totalqty > 1)
                                    System.out.println("The seats you requested are unavailable but we have other seats from " + bestSeat.getRow() + bestSeat.getSeat() + " to " + bestSeat.getRow() + (char)((int)bestSeat.getSeat() + totalqty - 1));
                                else if (totalqty == 1) //SPECIAL CASE Only one seat
                                    System.out.println("The seat you requested is unavailable but we have another seat at " + bestSeat.getRow() + bestSeat.getSeat());
                                System.out.println("Would you like to reserve the best available seats found?(Y/N)");
                                do
                                {
                                    while(true)
                                    {
                                        try
                                        {
                                            String str = sc.nextLine();
                                            if (str.length() == 1)
                                            {
                                                bestAns = Character.toUpperCase(str.charAt(0));
                                                break;
                                            }
                                        }
                                        catch(StringIndexOutOfBoundsException ex)
                                        {
                                            System.out.println("Invalid input. Please enter Y/N.");
                                        }
                                    }
                                    if (bestAns == 'Y' || bestAns == 'N')
                                        break;
                                    else
                                        System.out.println("Invalid input. Please enter Y/N.");
                                }while(true);
                                if (bestAns == 'Y')
                                {
                                    theatre.reserveSeats(bestSeat.getRow(), (int)(bestSeat.getSeat() - 64), noAdult, noChild, noSenior);
                                    if (totalqty > 0)
                                        System.out.println("Great! Your seats have been reserved.");
                                    PrintWriter output = new PrintWriter(audi); //To write back to original file
                                    for (int i = 0; i < rows; i++)
                                    {
                                        for (int j = 0; j < columns; j++)
                                        {
                                            output.print(theatre.getTheaterSeat(i, j).ticketType);
                                        }
                                        output.println();
                                    }
                                    output.flush();
                                    output.close();
                                    temp.ord.add(new Orders(audichoice, bestSeat.getRow(), (int)(bestSeat.getSeat() - 64), noAdult, noChild, noSenior));
                                }
                                else
                                {
                                    System.out.println("Sorry, the seats you have requested are unavailable. Please try with different seat selection.");
                                    continue;
                                }
                            }
                            else
                            {
                                System.out.println("Sorry, the seats you have requested are unavailable. Please try with different seat selection.");
                                continue;
                            }
                        }
                        if (choice == 1 && totalqty == 0) //If user tries to reserve 0 seats
                            System.out.println("Invalid input. Please try reserving again");
                    }
                    else if (choice == 2)
                    {
                        if (temp.ord.size() <= 0)
                        {
                            System.out.println("You have not placed any orders yet.");
                            continue;
                        }
                        int totTickets = 0, aduTickets = 0, chiTickets = 0, senTickets = 0;
                        String seats;
                        System.out.printf("%-30s %-30s %-30s %-30s %-30s %-30s %-30s\n", "Order Number", "Auditorium Number", "Total number of Tickets", "Seats", "Number of Adult Tickets", "Number of Child Tickets", "Number of Senior Tickets");
                        for (int i = 0; i < temp.ord.size(); i++)
                        {
                            seats = "";
                            totTickets = aduTickets = chiTickets = senTickets = 0;
                            totTickets += temp.ord.get(i).nA + temp.ord.get(i).nC + temp.ord.get(i).nS;
                            aduTickets += temp.ord.get(i).nA;
                            chiTickets += temp.ord.get(i).nC;
                            senTickets += temp.ord.get(i).nS;
                            for (int j = 0; j < totTickets; j++)
                            {
                                seats += temp.ord.get(i).rN;
                                seats += (char)(temp.ord.get(i).sS + 64 + j) + ",";
                            }
                            for (int j = 0; j < temp.ord.get(i).newOrders.size(); j++)
                            {
                                totTickets += temp.ord.get(i).newOrders.get(j).nA + temp.ord.get(i).newOrders.get(j).nC + temp.ord.get(i).newOrders.get(j).nS;
                                aduTickets += temp.ord.get(i).newOrders.get(j).nA;
                                chiTickets += temp.ord.get(i).newOrders.get(j).nC;
                                senTickets += temp.ord.get(i).newOrders.get(j).nS;
                                for (int k = 0; k < temp.ord.get(i).newOrders.get(j).nA + temp.ord.get(i).newOrders.get(j).nC + temp.ord.get(i).newOrders.get(j).nS; k++)
                                {
                                    seats += temp.ord.get(i).newOrders.get(j).rN;
                                    seats += (char)(temp.ord.get(i).newOrders.get(j).sS + 64 + j) + ",";
                                }
                            }
                            if (!seats.equals(""))
                                seats = seats.substring(0, seats.length() - 1);
                            System.out.printf("%-30s %-30s %-30s %-30s %-30s %-30s %-30s\n", i+1, temp.ord.get(i).audiNumber, totTickets, seats, aduTickets, chiTickets, senTickets);
                        }
                    }
                    else if (choice == 3)
                    {
                        if (temp.ord.size() <= 0)
                        {
                            System.out.println("You have not placed any orders yet.");
                            continue;
                        }
                        int totTickets = 0, aduTickets = 0, chiTickets = 0, senTickets = 0;
                        String seats;
                        System.out.printf("%-30s %-30s %-30s %-30s %-30s %-30s %-30s\n", "Order Number", "Auditorium Number", "Total number of Tickets", "Seats", "Number of Adult Tickets", "Number of Child Tickets", "Number of Senior Tickets");
                        for (int i = 0; i < temp.ord.size(); i++)
                        {
                            seats = "";
                            totTickets = aduTickets = chiTickets = senTickets = 0;
                            totTickets += temp.ord.get(i).nA + temp.ord.get(i).nC + temp.ord.get(i).nS;
                            aduTickets += temp.ord.get(i).nA;
                            chiTickets += temp.ord.get(i).nC;
                            senTickets += temp.ord.get(i).nS;
                            for (int j = 0; j < totTickets; j++)
                            {
                                seats += temp.ord.get(i).rN;
                                seats += (char)(temp.ord.get(i).sS + 64 + j) + ",";
                            }
                            for (int j = 0; j < temp.ord.get(i).newOrders.size(); j++)
                            {
                                totTickets += temp.ord.get(i).newOrders.get(j).nA + temp.ord.get(i).newOrders.get(j).nC + temp.ord.get(i).newOrders.get(j).nS;
                                aduTickets += temp.ord.get(i).newOrders.get(j).nA;
                                chiTickets += temp.ord.get(i).newOrders.get(j).nC;
                                senTickets += temp.ord.get(i).newOrders.get(j).nS;
                                for (int k = 0; k < temp.ord.get(i).newOrders.get(j).nA + temp.ord.get(i).newOrders.get(j).nC + temp.ord.get(i).newOrders.get(j).nS; k++)
                                {
                                    seats += temp.ord.get(i).newOrders.get(j).rN;
                                    seats += (char)(temp.ord.get(i).newOrders.get(j).sS + 64 + j) + ",";
                                }
                            }
                            if (!seats.equals(""))
                                seats = seats.substring(0, seats.length() - 1);
                            System.out.printf("%-30s %-30s %-30s %-30s %-30s %-30s %-30s\n", i+1, temp.ord.get(i).audiNumber, totTickets, seats, aduTickets, chiTickets, senTickets);
                        }
                        System.out.println("Please enter the order number that you would like to update.");
                        int oNo = 0;
                        do
                        {
                            do
                            {
                                try
                                {
                                    String str = sc.nextLine();
                                    oNo = Integer.parseInt(str);
                                    break;
                                }
                                catch(NumberFormatException ex)
                                {
                                    System.out.println("Invalid input. Please enter a valid order number.");
                                }
                            }while(true);
                            if (oNo > 0 && oNo <= temp.ord.size())
                                break;
                            else
                                System.out.println("Invalid input. Please enter a valid order number.");
                        }while(true);
                        do
                        {
                            System.out.println("1. Add tickets to order");
                            System.out.println("2. Delete tickets from order");
                            System.out.println("3. Cancel order");
                            int updateChoice = 0;
                            do
                            {
                                do
                                {
                                    try
                                    {
                                        String str = sc.nextLine();
                                        updateChoice = Integer.parseInt(str);
                                        break;
                                    }
                                    catch(NumberFormatException ex)
                                    {
                                        System.out.println("Invalid input. Please enter a number betwen 1-3.");
                                    }
                                }while(true);
                                if (updateChoice > 0 && oNo <= 3)
                                    break;
                                else
                                    System.out.println("Invalid input. Please enter a number between 1-3.");
                            }while(true);
                            if (updateChoice == 1)
                            {
                                if (oNo - 1 > temp.ord.size())
                                    break;
                                File audi = new File("A" + temp.ord.get(oNo - 1).audiNumber + ".txt");
                                if (!audi.exists())
                                    System.exit(0);
                                Scanner col = new Scanner(audi);
                                BufferedReader br = new BufferedReader(new FileReader(audi));
                                br.mark(260);   //Mark at 260 because max no. of rows x max no. of columns = 26 x 10 = 260
                                while(br.readLine() != null)
                                {
                                    rows++;
                                }
                                if (rows == 0)
                                {
                                    System.out.println("File empty");
                                    System.exit(0);
                                }
                                br.reset();
                                if ((br.readLine().length() == 1) && (rows == 1))   //Special Case if only ONE Seat
                                    columns++;
                                else
                                {
                                    while (col.hasNext())
                                    {
                                        columns = col.nextLine().length();
                                    }
                                }
                                br.reset(); //Reset BufferedReader pointer
                                Auditorium theatre = new Auditorium(rows, columns, br);
                                theatre.displayAuditorium(rows, columns);
                                System.out.println("Enter row number");
                                do
                                {
                                    while(true)
                                    {
                                        try
                                        {
                                            String str = sc.nextLine();
                                            rowNo = Integer.parseInt(str);
                                            break;
                                        }
                                        catch(NumberFormatException ex)
                                        {
                                            System.out.println("Invalid input. Please enter a valid row number.");
                                        }
                                    }
                                    if (rowNo > 0 && rowNo <= rows)
                                        break;
                                    else
                                        System.out.println("Invalid input. Please enter a valid row number.");
                                }while(true);
                                System.out.println("Enter starting seat letter");
                                do
                                {
                                    while(true)
                                    {
                                        try
                                        {
                                            String str = sc.nextLine();
                                            if (str.length() == 1)
                                            {
                                                startSeat = Character.toUpperCase(str.charAt(0));
                                                break;
                                            }
                                            else
                                            {
                                                startSeat = '0';
                                                break;
                                            }
                                        }
                                        catch(StringIndexOutOfBoundsException ex)
                                        {
                                            System.out.println("Invalid input. Please enter a valid starting seat letter.");
                                        }
                                    }
                                    if ((int)startSeat - 64 > 0 && (int)startSeat - 64 <= columns)
                                        break;
                                    else
                                        System.out.println("Invalid input. Please enter a valid starting seat letter.");
                                }while(true);
                                System.out.println("Enter number of adult tickets");
                                do
                                {
                                    while(true)
                                    {
                                        try
                                        {
                                            String str = sc.nextLine();
                                            noAdult = Integer.parseInt(str);
                                            break;
                                        }
                                        catch(NumberFormatException ex)
                                        {
                                            System.out.println("Invalid input. Please enter a valid number of tickets.");
                                        }
                                    }
                                    if (noAdult >= 0)
                                        break;
                                    else
                                        System.out.println("Invalid input. Please enter a valid number of tickets.");
                                }while(true);
                                System.out.println("Enter number of child tickets");
                                do
                                {
                                    while(true)
                                    {
                                        try
                                        {
                                            String str = sc.nextLine();
                                            noChild = Integer.parseInt(str);
                                            break;
                                        }
                                        catch(NumberFormatException ex)
                                        {
                                            System.out.println("Invalid input. Please enter a valid number of tickets.");
                                        }
                                    }
                                    if (noChild >= 0)
                                        break;
                                    else
                                        System.out.println("Invalid input. Please enter a valid number of tickets.");
                                }while(true);
                                System.out.println("Enter number of senior tickets");
                                do
                                {
                                    while(true)
                                    {
                                        try
                                        {
                                            String str = sc.nextLine();
                                            noSenior = Integer.parseInt(str);
                                            break;
                                        }
                                        catch(NumberFormatException ex)
                                        {
                                            System.out.println("Invalid input. Please enter a valid number of tickets.");
                                        }
                                    }
                                    if (noSenior >= 0)
                                        break;
                                    else
                                        System.out.println("Invalid input. Please enter a valid number of tickets.");
                                }while(true);
                                totalqty = noAdult + noChild + noSenior;
                                int seatStart = (int)startSeat - 64;
                                if(theatre.checkAvailability(rowNo, seatStart, totalqty))
                                {
                                    theatre.reserveSeats(rowNo, seatStart, noAdult, noChild, noSenior);
                                    if (totalqty > 0)
                                        System.out.println("Great! Your seats have been reserved.");
                                    PrintWriter output = new PrintWriter(audi); //To write back to original file
                                    for (int i = 0; i < rows; i++)
                                    {
                                        for (int j = 0; j < columns; j++)
                                        {
                                            output.print(theatre.getTheaterSeat(i, j).ticketType);
                                        }
                                        output.println();
                                    }
                                    output.flush();
                                    output.close();
                                    temp.ord.get(oNo - 1).newOrders.add(new Orders(temp.ord.get(oNo - 1).audiNumber, rowNo, seatStart, noAdult, noChild, noSenior));
                                    break;
                                }
                                else
                                {
                                    System.out.println("Sorry, the seats you have requested are unavailable.");
                                    continue;
                                }
                            }
                            else if (updateChoice == 2)
                            {
                                if (oNo - 1 > temp.ord.size())
                                    break;
                                File audi = new File("A" + temp.ord.get(oNo - 1).audiNumber + ".txt");
                                if (!audi.exists())
                                    System.exit(0);
                                Scanner col = new Scanner(audi);
                                BufferedReader br = new BufferedReader(new FileReader(audi));
                                br.mark(260);   //Mark at 260 because max no. of rows x max no. of columns = 26 x 10 = 260
                                while(br.readLine() != null)
                                {
                                    rows++;
                                }
                                if (rows == 0)
                                {
                                    System.out.println("File empty");
                                    System.exit(0);
                                }
                                br.reset();
                                if ((br.readLine().length() == 1) && (rows == 1))   //Special Case if only ONE Seat
                                    columns++;
                                else
                                {
                                    while (col.hasNext())
                                    {
                                        columns = col.nextLine().length();
                                    }
                                }
                                br.reset(); //Reset BufferedReader pointer
                                Auditorium theatre = new Auditorium(rows, columns, br);
                                int delRow = 0;
                                char delSeat;
                                System.out.println("Enter row number for the seat you want to delete.");
                                do
                                {
                                    while(true)
                                    {
                                        try
                                        {
                                            String str = sc.nextLine();
                                            delRow = Integer.parseInt(str);
                                            break;
                                        }
                                        catch(NumberFormatException ex)
                                        {
                                            System.out.println("Invalid input. Please enter a valid row number.");
                                        }
                                    }
                                    if (delRow > 0 && delRow <= rows)
                                        break;
                                    else
                                        System.out.println("Invalid input. Please enter a valid row number.");
                                }while(true);
                                System.out.println("Enter seat letter for the seat you want to delete.");
                                do
                                {
                                    while(true)
                                    {
                                        try
                                        {
                                            String str = sc.nextLine();
                                            if (str.length() == 1)
                                            {
                                                delSeat = Character.toUpperCase(str.charAt(0));
                                                break;
                                            }
                                            else
                                            {
                                                delSeat = '0';
                                                break;
                                            }
                                        }
                                        catch(StringIndexOutOfBoundsException ex)
                                        {
                                            System.out.println("Invalid input. Please enter a valid starting seat letter.");
                                        }
                                    }
                                    if ((int)delSeat - 64 > 0 && (int)delSeat - 64 <= columns)
                                        break;
                                    else
                                        System.out.println("Invalid input. Please enter a valid starting seat letter.");
                                }while(true);
                                boolean seatFound = false;
                                for (int i = 0; i < temp.ord.size(); i++)
                                {
                                    for (int j = 0; j < temp.ord.get(i).nA + temp.ord.get(i).nS + temp.ord.get(i).nC; j++)
                                    {
                                        if (temp.ord.get(i).rN == delRow && (char)(temp.ord.get(i).sS + 64 + j) == delSeat)
                                        {
                                            seatFound = true;
                                            char delType = theatre.getTheaterSeat(delRow - 1, (int)delSeat - 65).ticketType;
                                            theatre.setTheaterSeat(delRow - 1, (int)delSeat - 65, '.');
                                            if (delType == 'A')
                                                temp.ord.get(i).nA--;
                                            else if (delType == 'S')
                                                temp.ord.get(i).nS--;
                                            else if (delType == 'C')
                                                temp.ord.get(i).nC--;
                                            PrintWriter output = new PrintWriter(audi); //To write back to original file
                                            for (int k = 0; k < rows; k++)
                                            {
                                                for (int l = 0; l < columns; l++)
                                                {
                                                    output.print(theatre.getTheaterSeat(k, l).ticketType);
                                                }
                                                output.println();
                                            }
                                            output.flush();
                                            output.close();
                                            break;
                                        }
                                        if (seatFound == true)
                                            break;
                                    }
                                    for (int j = 0; j < temp.ord.get(i).newOrders.size(); j++)
                                    {
                                        for (int l = 0; l < temp.ord.get(i).newOrders.get(j).nA + temp.ord.get(i).newOrders.get(j).nC + temp.ord.get(i).newOrders.get(j).nS; l++)
                                        {
                                            if (temp.ord.get(i).newOrders.get(j).rN == delRow && (char)(temp.ord.get(i).newOrders.get(j).sS + 64 + l) == delSeat)
                                            {
                                                seatFound = true;
                                                char delType = theatre.getTheaterSeat(delRow - 1, (int)delSeat - 65).ticketType;
                                                theatre.setTheaterSeat(delRow - 1, (int)delSeat - 65, '.');
                                                if (delType == 'A')
                                                    temp.ord.get(i).newOrders.get(j).nA--;
                                                else if (delType == 'S')
                                                    temp.ord.get(i).newOrders.get(j).nS--;
                                                else if (delType == 'C')
                                                    temp.ord.get(i).newOrders.get(j).nC--;
                                                PrintWriter output = new PrintWriter(audi); //To write back to original file
                                                for (int k = 0; k < rows; k++)
                                                {
                                                    for (int m = 0; m < columns; m++)
                                                    {
                                                        output.print(theatre.getTheaterSeat(k, m).ticketType);
                                                    }
                                                    output.println();
                                                }
                                                output.flush();
                                                output.close();
                                                break;
                                            }
                                            if (seatFound == true)
                                                break;
                                        }
                                        if (seatFound == true)
                                            break;
                                    }
                                    if (seatFound == true)
                                        break;
                                }
                                if (seatFound == false)
                                {
                                    System.out.println("Sorry, the seat you have want to delete was not reserved in this order.");
                                    continue;
                                }
                                int tT = 0, tempT = 0;
                                for (int i = 0; i < temp.ord.size(); i++)
                                {
                                    tT += temp.ord.get(i).nA + temp.ord.get(i).nC + temp.ord.get(i).nS;
                                    for (int j = 0; j < temp.ord.get(i).newOrders.size(); j++)
                                    {
                                        tempT += temp.ord.get(i).newOrders.get(j).nA + temp.ord.get(i).newOrders.get(j).nC + temp.ord.get(i).newOrders.get(j).nS;
                                        tT += temp.ord.get(i).newOrders.get(j).nA + temp.ord.get(i).newOrders.get(j).nC + temp.ord.get(i).newOrders.get(j).nS;
                                        if (tempT <= 0)
                                        {
                                            temp.ord.get(i).newOrders.remove(j);
                                            i--;
                                        }
                                        tempT = 0;
                                    }
                                    if (tT <= 0)
                                    {
                                        temp.ord.remove(oNo - 1);
                                        i--;
                                    }
                                    tT = 0;
                                }
                                break;
                            }
                            else
                            {
                                if (oNo - 1 > temp.ord.size())
                                    break;
                                File audi = new File("A" + temp.ord.get(oNo - 1).audiNumber + ".txt");
                                if (!audi.exists())
                                    System.exit(0);
                                Scanner col = new Scanner(audi);
                                BufferedReader br = new BufferedReader(new FileReader(audi));
                                br.mark(260);   //Mark at 260 because max no. of rows x max no. of columns = 26 x 10 = 260
                                while(br.readLine() != null)
                                {
                                    rows++;
                                }
                                if (rows == 0)
                                {
                                    System.out.println("File empty");
                                    System.exit(0);
                                }
                                br.reset();
                                if ((br.readLine().length() == 1) && (rows == 1))   //Special Case if only ONE Seat
                                    columns++;
                                else
                                {
                                    while (col.hasNext())
                                    {
                                        columns = col.nextLine().length();
                                    }
                                }
                                br.reset(); //Reset BufferedReader pointer
                                Auditorium theatre = new Auditorium(rows, columns, br);
                                for (int i = 0; i < temp.ord.size(); i++)
                                {
                                    if (oNo - 1 == i)
                                    {
                                        for (int j = 0; j < temp.ord.get(i).nA + temp.ord.get(i).nC + temp.ord.get(i).nS; j++)
                                        {
                                            theatre.setTheaterSeat(temp.ord.get(i).rN - 1, temp.ord.get(i).sS + j - 1, '.');
                                        }
                                        for (int j = 0; j < temp.ord.get(i).newOrders.size(); j++)
                                        {
                                            for (int k = 0; k < temp.ord.get(i).newOrders.get(j).nA + temp.ord.get(i).newOrders.get(j).nC + temp.ord.get(i).newOrders.get(j).nS; k++)
                                            {
                                                theatre.setTheaterSeat(temp.ord.get(i).newOrders.get(j).rN - 1, temp.ord.get(i).newOrders.get(j).sS + k - 1, '.');
                                            }
                                        }
                                        PrintWriter output = new PrintWriter(audi); //To write back to original file
                                        for (int k = 0; k < rows; k++)
                                        {
                                            for (int l = 0; l < columns; l++)
                                            {
                                                output.print(theatre.getTheaterSeat(k, l).ticketType);
                                            }
                                            output.println();
                                        }
                                        output.flush();
                                        output.close();
                                        break;
                                    }
                                }
                                temp.ord.remove(oNo - 1);
                                break;
                            }
                        }while(true);
                    }
                    else if (choice == 4)
                    {
                        double orderAmt = 0, totAmt = 0;
                        if (temp.ord.size() <= 0)
                        {
                            System.out.println("You have not placed any orders yet.");
                            continue;
                        }
                        DecimalFormat df = new DecimalFormat(".00");    //Decimal format to round up to 2 decimal places
                        int totTickets = 0, aduTickets = 0, chiTickets = 0, senTickets = 0;
                        String seats;
                        System.out.printf("%-30s %-30s %-30s %-30s %-30s %-30s %-30s %-30s\n", "Order Number", "Auditorium Number", "Total number of Tickets", "Seats", "Number of Adult Tickets", "Number of Child Tickets", "Number of Senior Tickets", "Amount of Order");
                        for (int i = 0; i < temp.ord.size(); i++)
                        {
                            seats = "";
                            totTickets = aduTickets = chiTickets = senTickets = 0;
                            orderAmt = 0;
                            totTickets += temp.ord.get(i).nA + temp.ord.get(i).nC + temp.ord.get(i).nS;
                            aduTickets += temp.ord.get(i).nA;
                            chiTickets += temp.ord.get(i).nC;
                            senTickets += temp.ord.get(i).nS;
                            orderAmt += 10*temp.ord.get(i).nA + 5*temp.ord.get(i).nC + 7.5*temp.ord.get(i).nS;
                            for (int j = 0; j < totTickets; j++)
                            {
                                seats += temp.ord.get(i).rN;
                                seats += (char)(temp.ord.get(i).sS + 64 + j) + ",";
                            }
                            for (int j = 0; j < temp.ord.get(i).newOrders.size(); j++)
                            {
                                totTickets += temp.ord.get(i).newOrders.get(j).nA + temp.ord.get(i).newOrders.get(j).nC + temp.ord.get(i).newOrders.get(j).nS;
                                aduTickets += temp.ord.get(i).newOrders.get(j).nA;
                                chiTickets += temp.ord.get(i).newOrders.get(j).nC;
                                senTickets += temp.ord.get(i).newOrders.get(j).nS;
                                orderAmt += 10*temp.ord.get(i).newOrders.get(j).nA + 5*temp.ord.get(i).newOrders.get(j).nC + 7.5*temp.ord.get(i).newOrders.get(j).nS;
                                for (int k = 0; k < temp.ord.get(i).newOrders.get(j).nA + temp.ord.get(i).newOrders.get(j).nC + temp.ord.get(i).newOrders.get(j).nS; k++)
                                {
                                    seats += temp.ord.get(i).newOrders.get(j).rN;
                                    seats += (char)(temp.ord.get(i).newOrders.get(j).sS + 64 + j) + ",";
                                }
                            }
                            if (!seats.equals(""))
                                seats = seats.substring(0, seats.length() - 1);
                            totAmt += orderAmt;
                            System.out.printf("%-30s %-30s %-30s %-30s %-30s %-30s %-30s %-30s\n", i+1, temp.ord.get(i).audiNumber, totTickets, seats, aduTickets, chiTickets, senTickets, "$" + df.format(orderAmt));
                        }
                        System.out.printf("%-30s %-30s\n", "The overall amount of all orders is: ", "$" + df.format(totAmt));
                    }
                }while(choice != 5);
            }while(true);
        }
    }
}
