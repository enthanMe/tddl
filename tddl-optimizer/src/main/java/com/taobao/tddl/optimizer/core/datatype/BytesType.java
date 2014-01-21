package com.taobao.tddl.optimizer.core.datatype;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.common.primitives.Bytes;
import com.taobao.tddl.common.exception.NotSupportException;
import com.taobao.tddl.common.exception.TddlRuntimeException;
import com.taobao.tddl.common.model.BaseRowSet;

/**
 * {@link Bytes} 类型
 * 
 * @author mengshi.sunmengshi 2014年1月21日 下午5:16:00
 * @since 5.1.0
 */
public class BytesType extends AbstractDataType<byte[]> {

    @Override
    public int encodeToBytes(Object value, byte[] dst, int offset) {
        return DataEncoder.encode(this.convertFrom(value), dst, offset);
    }

    @Override
    public int getLength(Object value) {
        if (value == null) {
            return 1;
        } else {
            return DataEncoder.calculateEncodedLength((byte[]) value);
        }
    }

    @Override
    public DecodeResult decodeFromBytes(byte[] bytes, int offset) {
        try {
            byte[][] data = new byte[0][];
            int length = DataDecoder.decode(bytes, offset, data);
            return new DecodeResult(data[0], length);
        } catch (CorruptEncodingException e) {
            throw new TddlRuntimeException(e);
        }
    }

    @Override
    public byte[] incr(Object value) {
        throw new NotSupportException("bytes类型不支持incr操作");
    }

    @Override
    public byte[] decr(Object value) {
        throw new NotSupportException("bytes类型不支持decr操作");
    }

    @Override
    public byte[] getMaxValue() {
        return new byte[] { Byte.MAX_VALUE };
    }

    @Override
    public byte[] getMinValue() {
        return new byte[] { Byte.MIN_VALUE };
    }

    @Override
    public ResultGetter getResultGetter() {
        return new ResultGetter() {

            @Override
            public Object get(ResultSet rs, int index) throws SQLException {
                return rs.getBytes(index);
            }

            @Override
            public Object get(BaseRowSet rs, int index) {
                Object val = rs.getObject(index);
                return convertFrom(val);
            }
        };
    }

    @Override
    public int compare(Object o1, Object o2) {
        return 0;
    }

    @Override
    public Calculator getCalculator() {
        return null;
    }

}