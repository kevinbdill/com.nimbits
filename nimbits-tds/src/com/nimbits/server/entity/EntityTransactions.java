package com.nimbits.server.entity;

import com.nimbits.client.enums.*;
import com.nimbits.client.exception.*;
import com.nimbits.client.model.entity.*;

import java.util.*;

/**
 * Created by Benjamin Sautner
 * User: bsautner
 * Date: 2/28/12
 * Time: 11:46 AM
 */
public interface EntityTransactions {

    Map<String, Entity> getEntityMap(EntityType type);

    Map<EntityName, Entity> getEntityNameMap(EntityType type);

    List<Entity> getEntityChildren(Entity parentEntity, EntityType type);

    Entity addUpdateEntity(Entity entity) throws NimbitsException;

    List<Entity> getEntities();

    void deleteEntity(Entity entity) throws NimbitsException;

    Entity getEntityByUUID(String uuid);

    Entity getEntityByName(EntityName name);

    Map<String, Entity> getSystemWideEntityMap(EntityType type);
}
