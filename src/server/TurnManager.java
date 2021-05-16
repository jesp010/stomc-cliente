package server;

import control.ControlTurn;
import dominio.Employee;
import dominio.Message;
import dominio.Turn;
import main.ClientFrontController;
import main.TurneroController;

import java.util.*;


public class TurnManager {

    private static String separador = "\\s*-\\s*";

    private static final ControlTurn controlTurn = new ControlTurn();

    static TurneroController turneroController;
    static ClientFrontController clientFrontController;

    static int cajaTurnNumberFloor = 0;
    static int moduloTurnNumberFloor = 0;

    static int currentCajaTurnNumber = cajaTurnNumberFloor;
    static int currentModuloTurnNumber = moduloTurnNumberFloor;

    static List<Turn> cajaTurnList = new ArrayList<Turn>();
    static List<Turn> moduloTurnList = new ArrayList<Turn>();

    private static final Map<String, Message.MessageType> messageMap = new HashMap<String, Message.MessageType>();

    public static TurneroController getTurneroController() {
        return turneroController;
    }

    public static void setTurneroController(TurneroController turneroController) {
        TurnManager.turneroController = turneroController;
    }

    public static ClientFrontController getClientFrontController() {
        return clientFrontController;
    }

    public static void setClientFrontController(ClientFrontController clientFrontController) {
        TurnManager.clientFrontController = clientFrontController;
    }

    public static List<Turn> getCajaTurnList() {
        return cajaTurnList;
    }

    public static List<Turn> getModuloTurnList() {
        return moduloTurnList;
    }

    public static int getCajaTurnNumberFloor() {
        return cajaTurnNumberFloor;
    }

    public static int getModuloTurnNumberFloor() {
        return moduloTurnNumberFloor;
    }

    public static int getCurrentCajaTurnNumber() {
        return currentCajaTurnNumber;
    }

    public static int getCurrentModuloTurnNumber() {
        return currentModuloTurnNumber;
    }

    public Message handleRequest(Message message) {

        System.out.println("TurnManager received request of type: " + message.type);
        messageMap.put(message.getUuid(), message.getType());

        switch (message.type) {
//            case NEW_TURN_CAJA:
//                return createNewCajaTurn(message);
//            case NEW_TURN_MODULO:
//                return createNewModuloTurn(message);
//            case NEW_TURN_GENERIC:
//                return createNewGenericTurn(message);
            case CALL_NEXT_CAJA:
                return cajaCallNextTurn(message);
            case CALL_NEXT_MODULO:
                return moduloCallNextTurn(message);
            case RELEASE_TURN:
                return releaseTurn(message);
//            case GET_TURNS_STATUS:
//                return getTurnsStatus(message);
//            case GET_TURNS_START_DATE:
//                return getTurnsStartDate(message);
//            case GET_TURNS_END_DATE:
//                return getTurnsEndDate(message);
//            case GET_TURNS_STATUS_START_DATE:
//                return getTurnsStatusStartDate(message);
//            case GET_TURNS_STATUS_END_DATE:
//                return getTurnsStatusEndDate(message);
//            case GET_TURNS_START_AND_END_DATE:
//                return getTurnsStartEndDate(message);
//            case GET_TURNS_STATUS_START_AND_END_DATE:
//                return getTurnsStatusStartEndDate(message);
//            case GET_MANY_TURN:
//                return getTurns(message);
            default:
                throw new IllegalStateException("Unexpected value: " + message.type);
        }
    }

//    private Message getTurnsStatus(Message message) {
//        ArrayList<Object> contenido = (ArrayList<Object>) message.getObject();
//
//        String estado = (String) contenido.get(2);
//
//        List<Turn> turns = null;
//        try {
//            turns = controlTurn.findTurnByStatus(estado);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        message.setObject(turns);
//
//        return message;
//    }
//
//    private Message getTurnsStartDate(Message message) {
//        ArrayList<Object> contenido = (ArrayList<Object>) message.getObject();
//
//        Date fechaInicio = (Date) contenido.get(0);
//
//        List<Turn> turns = null;
//        try {
//            turns = controlTurn.Query_FechaInicio(fechaInicio);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        message.setObject(turns);
//
//        return message;
//    }
//
//    private Message getTurnsEndDate(Message message) {
//        ArrayList<Object> contenido = (ArrayList<Object>) message.getObject();
//
//        Date fechaFin = (Date) contenido.get(1);
//
//        List<Turn> turns = null;
//        try {
//            turns = controlTurn.Query_FechaFin(fechaFin);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        message.setObject(turns);
//
//        return message;
//    }
//
//    private Message getTurnsStatusStartDate(Message message) {
//        ArrayList<Object> contenido = (ArrayList<Object>) message.getObject();
//
//        Date fechaInicio = (Date) contenido.get(0);
//        String estado = (String) contenido.get(2);
//
//        List<Turn> turns = null;
//        try {
//            turns = controlTurn.Query_FechaInicioYEstado(estado, fechaInicio);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        message.setObject(turns);
//
//        return message;
//    }
//
//    private Message getTurnsStatusEndDate(Message message) {
//        ArrayList<Object> contenido = (ArrayList<Object>) message.getObject();
//
//        Date fechaFin = (Date) contenido.get(1);
//        String estado = (String) contenido.get(2);
//
//        List<Turn> turns = null;
//        try {
//            turns = controlTurn.Query_FechaFinYEstado(estado, fechaFin);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        message.setObject(turns);
//
//        return message;
//    }
//
//    private Message getTurnsStartEndDate(Message message) {
//        ArrayList<Object> contenido = (ArrayList<Object>) message.getObject();
//
//        Date fechaInicio = (Date) contenido.get(0);
//        Date fechaFin = (Date) contenido.get(1);
//
//        List<Turn> turns = null;
//        try {
//            turns = controlTurn.findTurnByDate(fechaInicio, fechaFin);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        message.setObject(turns);
//
//        return message;
//    }
//
//    private Message getTurnsStatusStartEndDate(Message message) {
//        ArrayList<Object> contenido = (ArrayList<Object>) message.getObject();
//
//        Date fechaInicio = (Date) contenido.get(0);
//        Date fechaFin = (Date) contenido.get(1);
//        String estado = (String) contenido.get(2);
//
//        List<Turn> turns = null;
//        try {
//            turns = controlTurn.Query_EntreFechasYEstado(estado, fechaInicio, fechaFin);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        message.setObject(turns);
//
//        return message;
//    }
//
//    private Message getTurns(Message message) {
//        List<Turn> turnos = controlTurn.findTurnEntities();
//        message.setObject(turnos);
//
//        return message;
//    }

    public static Message createNewCajaTurn(Message message){
        Turn newCajaTurn = new Turn();
        Employee empleado = (Employee) message.getObject();
        newCajaTurn.setIdEmployee(empleado);
        newCajaTurn.setTurnNumber(cajaTurnNumberFloor + 1);

        newCajaTurn.setType(Turn.Type.CAJA);
        newCajaTurn.setIsActive(true);
        newCajaTurn.setStatus("Esperando");
        newCajaTurn.setDateTimeCreated(new Date());

        cajaTurnNumberFloor += 1;

//        try {
//            controlTurn.create(newCajaTurn);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        cajaTurnList.add(newCajaTurn);
        System.out.println("TurnManager created turn: " + newCajaTurn.toString());

        message.setObject(newCajaTurn);

        clientFrontController.updateTurns();

        return message;
    }

    public static Message createNewModuloTurn(Message message){
        Turn newModuloTurn = new Turn();
        Employee empleado = (Employee) message.getObject();
        newModuloTurn.setIdEmployee(empleado);
        newModuloTurn.setTurnNumber(moduloTurnNumberFloor + 1);

        newModuloTurn.setType(Turn.Type.MODULO);
        newModuloTurn.setIsActive(true);
        newModuloTurn.setStatus("Esperando");
        newModuloTurn.setDateTimeCreated(new Date());

        moduloTurnNumberFloor += 1;

//        try {
//            controlTurn.create(newModuloTurn);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        moduloTurnList.add(newModuloTurn);
        System.out.println("TurnManager created turn: " + newModuloTurn.toString());

        message.setObject(newModuloTurn);

        clientFrontController.updateTurns();

        return message;
    }

    /*public static Message createNewGenericTurn(Message message){
        Turn newGenericTurn = new Turn();
        newGenericTurn.setTurnNumber(genericTurnNumberFloor + 1);

        newGenericTurn.setType(Turn.Type.GENERIC);
        newGenericTurn.setIsActive(true);
        newGenericTurn.setStatus("Esperando");
        newGenericTurn.setDateTimeCreated(new Date());

        genericTurnNumberFloor += 1;

//        try {
//            controlTurn.create(newGenericTurn);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        genericTurnList.add(newGenericTurn);
        System.out.println("TurnManager created turn: " + newGenericTurn.toString());

        message.setObject(newGenericTurn);

        return message;
    }*/

    public static Message cajaCallNextTurn(Message message){
        if (!cajaTurnList.isEmpty()) {
            Employee empleado = (Employee) message.getObject();

            Turn nextTurn = cajaTurnList.get(0);

            nextTurn.setStatus("Atendiendo");
            nextTurn.setDateTimeAssigned(new Date());
            nextTurn.setIdEmployee(empleado);

            System.out.println("TurnManager Next turn is: " + nextTurn.toString());

            try {
                controlTurn.edit(nextTurn);
            } catch (Exception e) {
                e.printStackTrace();
            }

            currentCajaTurnNumber = nextTurn.getTurnNumber();

            switch (empleado.getIdAttentionPoint().getPoint().split(separador)[0]) {
                case "Caja/Módulo":
                case "Caja/Modulo":
                    getTurneroController().siguienteTurnoCajaModulo("C", currentCajaTurnNumber, empleado.getIdAttentionPoint().getPoint());
                    break;
                default:
                    getTurneroController().siguienteTurnoCaja(currentCajaTurnNumber, empleado.getIdAttentionPoint().getPoint());
                    break;
            }


            cajaTurnList.remove(0);

            message.setObject(nextTurn);

            System.out.println("TurnManager Remaining Caja turns: " + cajaTurnList.size());

            clientFrontController.updateTurns();

            return message;
        } else {
            System.out.println("TurnManager No remaining Turns in Caja");
        }

        message.setObject(null);

        clientFrontController.updateTurns();

        return message;
    }

    public static Message moduloCallNextTurn(Message message){
        if (!moduloTurnList.isEmpty()) {

            Employee empleado = (Employee) message.getObject();

            Turn temp = moduloTurnList.get(0);

            temp.setStatus("Atendiendo");
            temp.setDateTimeAssigned(new Date());
            temp.setIdEmployee(empleado);

            try {
                controlTurn.edit(temp);
            } catch (Exception e) {
                e.printStackTrace();
            }

            currentModuloTurnNumber = temp.getTurnNumber();

            switch (empleado.getIdAttentionPoint().getPoint().split(separador)[0]) {
                case "Caja/Módulo":
                case "Caja/Modulo":
                    getTurneroController().siguienteTurnoCajaModulo("M", currentModuloTurnNumber, empleado.getIdAttentionPoint().getPoint());
                    break;
                default:
                    getTurneroController().siguienteTurnoModulo(currentModuloTurnNumber, empleado.getIdAttentionPoint().getPoint());
                    break;
            }

            moduloTurnList.remove(0);

            message.setObject(temp);

            System.out.println("TurnManager Remaining Modulo turns: " + moduloTurnList.size());

            clientFrontController.updateTurns();

            return message;
        } else {
            System.out.println("TurnManager No remaining Turns in Modulo");
        }

        message.setObject(null);

        clientFrontController.updateTurns();

        return message;
    }

    /*public static Message genericCallNextTurn(Message message){
        if (!genericTurnList.isEmpty()) {
            Employee empleado = (Employee) message.getObject();

            Turn temp = genericTurnList.get(0);

            temp.setStatus("Atendiendo");
            temp.setDateTimeAssigned(new Date());
            temp.setIdEmployee(empleado);

            try {
                controlTurn.edit(temp);
            } catch (Exception e) {
                e.printStackTrace();
            }

            currentGenericTurnNumber = temp.getTurnNumber();

            getTurneroController().siguienteTurnoCajaModulo(currentGenericTurnNumber, empleado.getIdAttentionPoint().getPoint());

            genericTurnList.remove(0);

            message.setObject(temp);

            System.out.println("TurnManager Remaining Generic turns: " + genericTurnList.size());

            return message;
        } else {
            System.out.println("TurnManager No remaining Turns in Generic");
        }

        message.setObject(null);

        return message;
    }*/

    public static Message releaseTurn(Message message){

        Turn turn = (Turn) message.getObject();

        turn.setDateTimeFinished(new Date());
        turn.setStatus("Finalizado");
        turn.setIsActive(false);

        try {
            controlTurn.edit(turn);
        } catch (Exception e) {
            e.printStackTrace();
        }

        message.setObject(turn);

        return message;
    }

    /**
     * Called when Server could not connect/send the requested turn
     * to a Client. Receives the turn and adds it back into the
     * applicable turn list at index 0. Only applies to a turn requested
     * via CALL_NEXT_*
     * @param turn
     */
    public void sendBackTurn(Turn turn){
        //FIXME: refactor to descriptive func name
        switch (turn.getType()) {
            case CAJA:
                cajaTurnList.add(0, turn);
                break;
            case MODULO:
                //TODO: logic modulo
                moduloTurnList.add(0, turn);
                break;
            /*case GENERIC:
                //TODO: logic generic
                genericTurnList.add(0, turn);
                break;*/
            default: throw new IllegalStateException("Unexpected value: " + turn.getType());
        }
    }

}
