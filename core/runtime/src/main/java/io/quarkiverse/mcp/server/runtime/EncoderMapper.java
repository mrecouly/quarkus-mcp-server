package io.quarkiverse.mcp.server.runtime;

import java.util.function.Function;

import io.quarkiverse.mcp.server.Encoder;
import io.quarkiverse.mcp.server.runtime.ResultMappers.Result;
import io.smallrye.mutiny.Uni;

/**
 * Marker interface for all mappers based on {@link Encoder}.
 */
public interface EncoderMapper<TYPE, RESPONSE> extends Function<Result<TYPE>, Uni<RESPONSE>> {

}