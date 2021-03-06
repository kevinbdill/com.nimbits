/*
 * Copyright (c) 2012 Nimbits Inc.
 *
 *    http://www.nimbits.com
 *
 *
 * Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/gpl.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the license is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, eitherexpress or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.nimbits.cloudplatform.server.transactions.entity;

import com.nimbits.cloudplatform.client.enums.EntityType;
import com.nimbits.cloudplatform.client.enums.ProtectionLevel;
import com.nimbits.cloudplatform.client.model.calculation.Calculation;
import com.nimbits.cloudplatform.client.model.calculation.CalculationModelFactory;
import com.nimbits.cloudplatform.client.model.common.impl.CommonFactory;
import com.nimbits.cloudplatform.client.model.entity.Entity;
import com.nimbits.cloudplatform.client.model.entity.EntityModelFactory;
import com.nimbits.cloudplatform.client.model.entity.EntityName;
import com.nimbits.cloudplatform.server.NimbitsServletTest;
import com.nimbits.cloudplatform.server.orm.CalcEntity;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: benjamin
 * Date: 8/8/12
 * Time: 10:21 AM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
        "classpath:META-INF/applicationContext.xml",
        "classpath:META-INF/applicationContext-api.xml",
        "classpath:META-INF/applicationContext-cache.xml",
        "classpath:META-INF/applicationContext-cron.xml",
        "classpath:META-INF/applicationContext-dao.xml",
        "classpath:META-INF/applicationContext-service.xml",
        "classpath:META-INF/applicationContext-task.xml"

})
public class EntityCacheImplTest extends NimbitsServletTest {



    @Test
    public void testRemoveEntityFromCache() throws Exception {
      
        EntityCache.addEntityToCache(user, Arrays.asList((Entity) point));

        List<Entity> e = EntityCache.getEntityFromCache(point.getKey());
        assertFalse(e.isEmpty());
        EntityCache.removeEntityFromCache(Arrays.asList((Entity)point));
        List<Entity> e2 = EntityCache.getEntityFromCache(point.getKey());
        assertTrue(e2.isEmpty());
    }

    @Test
    public void testGetEntityByName() throws Exception {

    }

    @Test
    public void testGetEntitiesBySource() throws Exception {

    }

    @Test
    public void testGetEntityByTrigger() throws Exception {
        EntityName name = CommonFactory.createName("calc", EntityType.calculation);

        Entity entity = EntityModelFactory.createEntity(name, "", EntityType.calculation, ProtectionLevel.onlyMe, point.getKey(), user.getKey());

        Calculation c = CalculationModelFactory.createCalculation(entity, EntityModelFactory.createTrigger(point.getKey()), true, "1+1", EntityModelFactory.createTarget(pointChild.getKey()), "", "", "");
        EntityServiceImpl.addUpdateEntity(user, c);
        List<Entity> triggers = EntityCache.getEntityByTrigger(user, point, CalcEntity.class);
        assertFalse(triggers.isEmpty());

        for (Entity e : triggers) {
            assertFalse(e.isCached());
        }

        List<Entity> triggers2 = EntityServiceImpl.getEntityByTrigger(user, point, EntityType.calculation);
        assertFalse(triggers2.isEmpty());

        for (Entity e : triggers2) {
            assertTrue(e.isCached());
        }


        EntityName name2 = CommonFactory.createName("calc2", EntityType.calculation);

        Entity entity2 = EntityModelFactory.createEntity(name2, "", EntityType.calculation, ProtectionLevel.onlyMe, point.getKey(), user.getKey());

        Calculation c2 = CalculationModelFactory.createCalculation(entity2, EntityModelFactory.createTrigger(point.getKey()), true, "1+2", EntityModelFactory.createTarget(pointChild.getKey()), "", "", "");
        EntityServiceImpl.addUpdateEntity(user, c2);

        List<Entity> triggers3 = EntityCache.getEntityByTrigger(user, point, CalcEntity.class);
        assertFalse(triggers3.isEmpty());

        for (Entity e : triggers3) {
            assertFalse(e.isCached());
        }

        List<Entity> triggers4 = EntityCache.getEntityByTrigger(user, point, CalcEntity.class);
        assertFalse(triggers4.isEmpty());

        for (Entity e : triggers4) {
            assertTrue(e.isCached());
        }


    }

    @Test
    public void testGetIdleEntities() throws Exception {

    }

    @Test
    public void testGetSubscriptionsToEntity() throws Exception {

    }

    @Test
    public void testGetEntityByBlobKey() throws Exception {

    }

    @Test
    public void testGetEntityMap() throws Exception {

    }

    @Test
    public void testGetEntityNameMap() throws Exception {

    }

    @Test
    public void testGetChildren() throws Exception {

    }

    @Test
    public void testAddUpdateEntity() throws Exception {

    }

    @Test
    @Ignore
    public void testGetEntities() throws Exception {
        List<Entity> results = EntityCache.getEntities(user);
        assertFalse(results.isEmpty());
        for (Entity e: results) {
            assertFalse(e.isCached());

        }

        List<Entity> results2 = EntityCache.getEntities(user);
        assertFalse(results2.isEmpty());
        for (Entity e: results2) {
            assertTrue(e.isCached());

        }
        point.setExpire(50);
        EntityCache.addUpdateEntity(user, Arrays.asList((Entity)point), true);

        List<Entity> results3 = EntityCache.getEntities(user);
        assertFalse(results3.isEmpty());
        for (Entity e: results3) {
            assertFalse(e.isCached());

        }

        List<Entity> results4 = EntityCache.getEntities(user);
        assertFalse(results4.isEmpty());
        for (Entity e: results4) {
            assertTrue(e.isCached());

        }
    }

    @Test
    public void testDeleteEntity() throws Exception {

    }

    @Test
    public void testGetEntityByKey() throws Exception {

    }

    @Test
    public void testGetSystemWideEntityMap() throws Exception {

    }
}
