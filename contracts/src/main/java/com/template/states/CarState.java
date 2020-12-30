package com.template.states;

import com.template.contracts.CarContract;
import com.template.contracts.TemplateContract;
import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;

import java.util.Arrays;
import java.util.List;

// *********
// * State *
// *********
@BelongsToContract(CarContract.class)
public class CarState implements ContractState {

    private final String model;
    private final Party owner;
    private final Party manufacturer;


    //private variables

    /* Constructor of your Corda state */
    public CarState(String model, Party owner, Party manufacturer) {
        this.model = model;
        this.owner = owner;
        this.manufacturer = manufacturer;
    }

    //getters
    public String getModel() { return model;    }
    public Party getOwner() { return owner;    }
    public Party getManufacturer() { return manufacturer;    }


    /* This method will indicate who are the participants and required signers when
     * this state is used in a transaction. */
    @Override
    public List<AbstractParty> getParticipants() {
        return Arrays.asList(owner, manufacturer);
    }

}