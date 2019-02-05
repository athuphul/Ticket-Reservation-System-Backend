//Atharva Phulambrikar
//amp170230

package Tickets;

import java.util.ArrayList;

public class Orders {
    int audiNumber = -1;
    int rN = -1;
    int sS = -1;
    int nA = 0;
    int nC = 0;
    int nS = 0;
    ArrayList <Orders> newOrders = new ArrayList<>();
    public Orders(int a, int r, int s, int A, int C, int S)
    {
        audiNumber = a;
        rN = r;
        sS = s;
        nA = A;
        nC = C;
        nS = S;
    }
}
