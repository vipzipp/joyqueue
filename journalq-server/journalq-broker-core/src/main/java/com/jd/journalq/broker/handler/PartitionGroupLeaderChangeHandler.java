/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jd.journalq.broker.handler;

import com.jd.journalq.broker.BrokerContext;
import com.jd.journalq.broker.election.ElectionService;
import com.jd.journalq.domain.PartitionGroup;
import com.jd.journalq.exception.JournalqCode;
import com.jd.journalq.network.command.BooleanAck;
import com.jd.journalq.network.transport.Transport;
import com.jd.journalq.network.transport.command.Command;
import com.jd.journalq.network.transport.command.Type;
import com.jd.journalq.network.transport.command.handler.CommandHandler;
import com.jd.journalq.nsr.network.command.NsrCommandType;
import com.jd.journalq.nsr.network.command.UpdatePartitionGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wylixiaobin
 * Date: 2019/3/7
 */
public class PartitionGroupLeaderChangeHandler implements CommandHandler, Type {
    private static Logger logger = LoggerFactory.getLogger(PartitionGroupLeaderChangeHandler.class);
    private ElectionService electionService;

    public PartitionGroupLeaderChangeHandler(BrokerContext brokerContext){
        this.electionService = brokerContext.getElectionService();
    }
    @Override
    public int type() {
        return NsrCommandType.NSR_LEADERCHANAGE_PARTITIONGROUP;
    }

    @Override
    public Command handle(Transport transport, Command command) {
        if (command == null) {
            logger.error("PartitionGroupLeaderChangeHandler request command is null");
            return null;
        }
        UpdatePartitionGroup request = (UpdatePartitionGroup) command.getPayload();
        PartitionGroup group = request.getPartitionGroup();
        try {
            electionService.onLeaderChange(group.getTopic(),group.getGroup(),group.getLeader());
        } catch (Exception e) {
            logger.error(String.format("PartitionGroupLeaderChangeHandler request command[%s] error", command.getPayload()), e);
            return BooleanAck.build(JournalqCode.CN_UNKNOWN_ERROR, e.getMessage());
        }
        return BooleanAck.build();
    }
}
