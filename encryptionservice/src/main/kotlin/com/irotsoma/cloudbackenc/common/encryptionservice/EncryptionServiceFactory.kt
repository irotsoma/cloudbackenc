/*
 * Copyright (C) 2016  Irotsoma, LLC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
/*
 * Created by irotsoma on 8/18/2016.
 */
package com.irotsoma.cloudbackenc.common.encryptionservice

/**
 * Encryption Service Factory interface
 *
 * @author Justin Zak
 */
interface EncryptionServiceFactory {
    /**
     * List of key algorithms that the extension supports
     *
     * @see EncryptionServiceKeyAlgorithms for list of possible values
     */
    val supportedKeyAlgorithms: Array<EncryptionServiceKeyAlgorithms>
    /**
     * List of encryption algorithms that the extension supports
     *
     * @see EncryptionServiceEncryptionAlgorithms for list of possible values
     */
    val supportedEncryptionAlgorithms: Array<EncryptionServiceEncryptionAlgorithms>
    /**
     * List of PBKDF Algorithms that the extension supports
     *
     * @see EncryptionServicePBKDFAlgorithms for list of possible values
     */
    val supportedPBKDFAlgorithms: Array<EncryptionServicePBKDFAlgorithms>
    /**
     * Service that handles generation and processing of encryption keys
     */
    val encryptionServiceKeyService: EncryptionServiceKeyService
    /**
     * Service that handles encryption and decryption of files
     */
    val encryptionServiceFileService: EncryptionServiceFileService
    /**
     * Service that handles encryption and decryption of strings
     */
    val encryptionServiceStringService: EncryptionServiceStringService
}