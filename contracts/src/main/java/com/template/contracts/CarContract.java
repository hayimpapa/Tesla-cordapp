package com.template.contracts;

import com.template.states.CarState;
import com.template.states.TemplateState;
import net.corda.core.contracts.*;
import net.corda.core.transactions.LedgerTransaction;
import org.jetbrains.annotations.NotNull;

import java.security.PublicKey;
import java.util.List;

import static net.corda.core.contracts.ContractsDSL.requireSingleCommand;
import static net.corda.core.contracts.ContractsDSL.requireThat;

// ************
// * Contract *
// ************
public class CarContract implements Contract {
    // This is used to identify our contract when building a transaction.
    public static final String CAR_CONTRACT_ID = "com.template.contracts.CarContract";

    @Override
    public void verify(@NotNull LedgerTransaction tx) throws IllegalArgumentException {
        if(tx.getCommands().size() != 1) throw new IllegalArgumentException("There can be only one command");
        Command command = tx.getCommand(0); //get the first command of this transaction
        CommandData commandType = command.getValue();
        //this one is added for the Signer rules - see later
        List<PublicKey> requiredSigners = command.getSigners();

        if (commandType instanceof Shipment) {
            //Shipment rules

            // 3 types of rules
            // shape rules - number of input output states are allowed
            // no input, 1 output
            if(tx.getInputStates().size() != 0) {
                throw new IllegalArgumentException("There cannot be input states");
            }
            if(tx.getOutputStates().size() != 1) {
                throw new IllegalArgumentException("Only one vehicle can be shipped at a time");
            }

            // content rules - what the contents of the input and output state can be
            // Except only model Cybertruck and also to check CarState

            ContractState outputState = tx.getOutput(0);

            if(!(outputState instanceof CarState)) {
                throw new IllegalArgumentException("Output has to be a type of CarState");
            }

            CarState carState = (CarState) outputState;
            if (!carState.getModel().equals("Cybertruck")) {
                throw new IllegalArgumentException("This is not a CyberTruck");
            }

            //signer  rules - who needs to sign the transactions
            // manufacturer sig should be mandatory -  had to add List above

            PublicKey manufacturerKey = carState.getManufacturer().getOwningKey();

            if (!(requiredSigners.contains(manufacturerKey))) {
                throw new IllegalArgumentException("Manufacturer must sign");
            }



        }
    }

    // A transaction is valid if the verify() function of the contract of all the transaction's input and output states
    // does not throw an exception.
        /*
    @Override
    public void verify(LedgerTransaction tx) {

         We can use the requireSingleCommand function to extract command data from transaction.
         * However, it is possible to have multiple commands in a signle transaction.
        final CommandWithParties<Commands> command = requireSingleCommand(tx.getCommands(), Commands.class);
        final Commands commandData = command.getValue();

        if (commandData.equals(new Commands.Send())) {
            //Retrieve the output state of the transaction
            TemplateState output = tx.outputsOfType(TemplateState.class).get(0);

            //Using Corda DSL function requireThat to replicate conditions-checks
            requireThat(require -> {
                require.using("No inputs should be consumed when sending the Hello-World message.", tx.getInputStates().size() == 0);
                require.using("The message must be Hello-World", output.getMsg().equals("Hello-World"));
                return null;
            });
        }
    }
*/

    // Used to indicate the transaction's intent.
 //   public interface Commands extends CommandData {
        //In our hello-world app, We will only have one command.
 //       class Send implements Commands {}
//    }

        public static class Shipment implements CommandData {}



}