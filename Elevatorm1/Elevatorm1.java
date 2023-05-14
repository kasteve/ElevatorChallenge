package Elevatorm1;

import OtherElevators.OtherElevators;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Elevatorm1 {
    private int currentFloor, numPassengers;
    public List<String> requests;
    private boolean isFull;

    public Elevatorm1() {
        this.currentFloor = 1;
        this.requests = new ArrayList<>();
        this.numPassengers = 0;
        this.isFull = false;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("\n\nEnter floor requests (e.g. 1:2,3:4,5:2): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            String[] parts = input.split(",");
            for (String part : parts) {
                if (!isFull) {
                    if (numPassengers < 10) {
                        requests.add(part);
                        numPassengers++;
                    } else {
                        System.out.println("\n\nSORRY, THE ELEVATOR IS FULL TO CAPACITY.\nANOTHER ELEVATOR IS COMING.\n");
                        isFull = true;
                        System.out.println("SENDING REQUESTS TO ANOTHER AVAILABLE ELEVATOR.\n");
                        // Code to send the requests to another available elevator
                        sendRequestsToAnotherElevator();
                        requests.clear();
                        break;
                    }
                } else {
                    System.out.println("ANOTHER ELEVATOR IS COMING.\n");
                    System.out.println("SENDING REQUESTS TO ANOTHER AVAILABLE ELEVATOR.\n");
                    // Code to send the requests to another available elevator
                    sendRequestsToAnotherElevator();
                    requests.clear();
                    break;
                }
            }
            if (!isFull) {
                handleRequests();
            }
        }
    }

    private void sendRequestsToAnotherElevator() {
        // Code to send the requests to another available elevator
        OtherElevators anotherElevator = new OtherElevators();
        anotherElevator.handleRequests();
    }


    public void handleRequests() {
        while(requests.size() > 0) {
            List<String> pickedUpRequests = new ArrayList<>();
            List<String> remainingRequests = new ArrayList<>();
            for (String request : requests) {
                int sourceFloor = 1;
                int destinationFloor = 1;
                try {
                    sourceFloor = Integer.parseInt(request.split(":")[0]);
                    destinationFloor = Integer.parseInt(request.split(":")[1]);
                } catch (NumberFormatException e) {
                    System.out.println("\nINVALID INPUT FORMAT: " + request);
                    continue;
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("\nINVALID INPUT FORMAT: " + request);
                    continue;
                }
                if (sourceFloor == currentFloor) {
                    pickedUpRequests.add(request);
                    numPassengers--;
                } else {
                    remainingRequests.add(request);
                }
            }
            if (!pickedUpRequests.isEmpty()) {
                Collections.sort(pickedUpRequests);
                System.out.println("\nPICKING UP REQUESTS: " + pickedUpRequests);
            }
            if (!remainingRequests.isEmpty()) {
                int nextFloor = getNextFloor(remainingRequests);
                System.out.println("\nGOING TO FLOOR " + nextFloor);
                currentFloor = nextFloor;
            } else {
                System.out.println("\nALL REQUESTS HAVE BEEN HANDLED.");
            }
            requests = remainingRequests;

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.err.println("Sleep interrupted: " + e.getMessage());
            }
        }
    }

    private int getNextFloor(List<String> requests) {
        int nextFloor = -1;
        int minDistance = Integer.MAX_VALUE;
        for (String request : requests) {
            int sourceFloor = 1;
            try {
                sourceFloor = Integer.parseInt(request.split(":")[0]);
            } catch (NumberFormatException e) {
                // should not happen since we've already validated the input
            } catch (ArrayIndexOutOfBoundsException e) {
                // should not happen since we've already validated the input
            }
            int distance = Math.abs(sourceFloor - currentFloor);
            if (distance < minDistance) {
                minDistance = distance;
                nextFloor = sourceFloor;
            }
        }
        return nextFloor;
    }

    public static void main(String[] args) {
        OtherElevators elevatorm = new OtherElevators();
        elevatorm.start();
    }
}
