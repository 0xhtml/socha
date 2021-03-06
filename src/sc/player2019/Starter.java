package sc.player2019;

import jargs.gnu.CmdLineParser;
import sc.player2019.logic.AlphaBeta;
import sc.plugin2019.AbstractClient;
import sc.shared.SharedConfiguration;

public class Starter extends AbstractClient {

    private Starter(String host, int port, String reservation) throws Exception {
        super(host, port);

        setHandler(new AlphaBeta(this));

        if (reservation == null || reservation.isEmpty()) {
            joinAnyGame();
        } else {
            joinPreparedGame(reservation);
        }
    }

    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-8");

        CmdLineParser parser = new CmdLineParser();
        CmdLineParser.Option hostOption = parser.addStringOption('h', "host");
        CmdLineParser.Option portOption = parser.addIntegerOption('p', "port");
        CmdLineParser.Option reservationOption = parser.addStringOption('r', "reservation");

        try {
            parser.parse(args);
        } catch (CmdLineParser.OptionException e) {
            System.exit(2);
        }

        String host = (String) parser.getOptionValue(hostOption, "localhost");
        int port = (Integer) parser.getOptionValue(portOption, SharedConfiguration.DEFAULT_PORT);
        String reservation = (String) parser.getOptionValue(reservationOption, "");

        try {
            new Starter(host, port, reservation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
