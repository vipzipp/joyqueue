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
package com.jd.journalq.network.command;

import com.jd.journalq.exception.JournalqCode;
import com.jd.journalq.network.transport.codec.JMQHeader;
import com.jd.journalq.network.transport.command.Command;
import com.jd.journalq.network.transport.command.Direction;
import com.jd.journalq.network.transport.command.JMQPayload;
import org.apache.commons.lang3.StringUtils;

/**
 * 布尔应答.
 *
 * @author lindeqiang
 * @since 2016/8/11 10:32
 */
public class BooleanAck extends JMQPayload {

    @Override
    public int type() {
        return CommandType.BOOLEAN_ACK;
    }

    /**
     * 构造布尔应答
     *
     * @return 布尔应答
     */
    public static Command build() {
        return build(JournalqCode.SUCCESS);
    }

    /**
     * 构造布尔应答
     *
     * @param code
     * @param args
     * @return
     */
    public static Command build(final JournalqCode code, Object... args) {
        return build(code.getCode(), code.getMessage(args));
    }

    /**
     * 构造布尔应答
     *
     * @param code 代码
     * @return 布尔应答
     */
    public static Command build(final int code) {
        return build(code, null);
    }

    /**
     * 构造布尔应答
     *
     * @param code    代码
     * @param message 消息
     * @return 布尔应答
     */
    public static Command build(final int code, final String message) {
        JMQHeader header = new JMQHeader(Direction.RESPONSE, CommandType.BOOLEAN_ACK);
        header.setStatus((short) code);
        header.setError(code == JournalqCode.SUCCESS.getCode() ? null : (StringUtils.isBlank(message) ? JournalqCode.valueOf(code).getMessage() : message));
        return new Command(header, null);
    }
}
