package org.xianwu.core.id;

/**
 * SequenceFormater
 * 此代码源于开源项目E3
 * 
 * @author XianwuFu
 * @since 2013-01-01
 */
public interface SequenceFormater {
	public String format(long pSequence) throws FormatSequenceExcepiton;
}
