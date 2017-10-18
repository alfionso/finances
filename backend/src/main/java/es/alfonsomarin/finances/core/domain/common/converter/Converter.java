/*
 * Copyright 2016-2007 Alfonso Marin Lopez.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at  
 * 	  http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.alfonsomarin.finances.core.domain.common.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * <p>{@code Converter} interface is implemented by any model class that
 * supports conversion between core and persistence domains.</p>
 * <p>
 * <p>{@code @Converter} also provides default implementations for the
 * conversion of typed object's collections ({@code List} or {@code Set}).</p>
 * <p>
 * Type parameters:
 * <ul>
 * <li><em>C</em> - Core domain object</li>
 * <li><em>P</em> - Persistence entity</li>
 * <li><em>CC</em> - Core object collection</li>
 * <li><em>PC</em> - Persistence entity collection</li>
 * </ul>
 *
 * @param <C> the type parameter
 * @param <P> the type parameter
 * @author alfonso.marin.lopez
 */
public interface Converter<C, P> {

    /**
     * Converts core entity to new persistence entity.
     *
     * @param c Core entity.
     * @return New mapped persistence entity.
     */
    P toPersistence(C c);

    /**
     * Converts core entity to given persistence entity.
     *
     * @param c Core entity.
     * @param p Persistence entity.
     * @return Mapped persistence entity.
     */
    P toPersistence(C c, P p);

    /**
     * Converts persistence entity to new core entity.
     *
     * @param p Persistence entity.
     * @return New mapped core entity.
     */
    C toCore(P p);

    /**
     * Default conversion of a list of core objects to
     * a list of persistence entities.
     *
     * @param <CC> the type parameter
     * @param <PC> the type parameter
     * @param cc   list of core objects
     * @return list of persistence entities
     */
    @SuppressWarnings("unchecked")
    default <CC extends Collection<C>, PC extends Collection<P>> PC toPersistence(CC cc) {

        if(ofNullable(cc).isPresent()) {
            Stream<P> stream = cc.stream()
                    .map(this::toPersistence);

            return (PC) (cc instanceof List ? stream.collect(toList()) : stream.collect(toSet()));
        }else{
            return (PC) (cc instanceof List ? new ArrayList<>() : new HashSet<>());
        }
    }

    /**
     * Default conversion of a list of persistence entities
     * to a list of core objects.
     *
     * @param <CC> the type parameter
     * @param <PC> the type parameter
     * @param pc   list of persistence entities
     * @return list of core objects
     */
    @SuppressWarnings("unchecked")
    default <CC extends Collection<C>, PC extends Collection<P>> CC toCore(PC pc) {

        if(ofNullable(pc).isPresent()){
            Stream<C> stream = pc.stream()
                    .map(this::toCore);

            return (CC) (pc instanceof List ? stream.collect(toList()) : stream.collect(toSet()));
        }
        else {
            return (CC) (pc instanceof List ? new ArrayList<>() : new HashSet<>());
        }
    }
}
