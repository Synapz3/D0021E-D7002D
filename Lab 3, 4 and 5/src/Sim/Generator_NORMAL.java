package Sim;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.lang.Math;

public class Generator_NORMAL extends Node {

    protected double _time = 0;
    protected Random randInt = new Random();
    protected int _std_deviation;
    protected int _mean;
    protected double time_limit = 0.0;

    public Generator_NORMAL (int network, int node) {
        super(node, node);
        _id = new NetworkAddr(network, node);
    }

    // A function to help print timestamps to a file
    private void log_time(String time, String file_name)
            throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file_name,true));
        writer.write(time + "\n");
        writer.close();
    }

    // Modified to only take a time limit, diviation and mean.
    public void StartSendingNormal(int network, int node, int std_diviation, int mean_pkg, double time_limit)
    {
        _toNetwork = network;
        _std_deviation = std_diviation;
        _mean = mean_pkg;
        _toHost = node;
        _seq = 1;
        this.time_limit = time_limit;
        send(this, new TimerEvent(), 0);
        System.out.println("Sending signal to start sending...");

    }

    // Override: Modified to send packages as a normal distribution with a random gaussian number.
    public void recv(SimEnt src, Event ev)
    {


        if (ev instanceof TimerEvent)
        {

            if (SimEngine.getTime() < time_limit)
            {

                int packages = (int)Math.abs(randInt.nextGaussian() * _std_deviation + _mean);
                _time = SimEngine.getTime();

                for (int i = 0; i < packages; i++)
                {

                    double zeroOne = Math.random();
                    double thisTime = SimEngine.getTime() + zeroOne;

                    try
                    {
                        log_time(Double.toString(thisTime), "NORMAL_Generator_Sending");
                    } catch (Exception e) {
                        // TODO: handle exception
                        System.out.println(e);
                    }

                    System.out.println("Time of sending package is: " + thisTime);

                    _sentmsg++;
                    send(_peer, new Message(_id, new NetworkAddr(_toNetwork, _toHost), _seq), zeroOne);
                    System.out.println("Node "+_id.networkId()+ "." + _id.nodeId() +" sent message with seq: "+_seq + " at time "+ thisTime);
                    _seq++;
                }

                send(this, new TimerEvent(),1);
            }
        }

        if (ev instanceof Message)
        {

            System.out.println("Node "+_id.networkId()+ "." + _id.nodeId() +" receives message with seq: "+((Message) ev).seq() + " at time "+SimEngine.getTime());
        }
    }

}
