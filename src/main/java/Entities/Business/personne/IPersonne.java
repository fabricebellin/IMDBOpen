package entities.business.personne;

import Entities.generic.IEntity;

import java.io.Serializable;

public interface IPersonne <ID extends Serializable> extends IEntity<ID> {
    String getIdentite();
    void setIdentite(String nom);


    String getDateNaissance();
    void setDateNaissance(String dateNaissance);
}
