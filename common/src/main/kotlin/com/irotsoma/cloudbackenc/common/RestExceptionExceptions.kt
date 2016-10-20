/*
 * Created by irotsoma on 10/20/16.
 */
package com.irotsoma.cloudbackenc.common

import java.util.*


enum class RestExceptionExceptions {
    /**
     * Returned when attempting to create a user with a user ID that already exists.
     */
    Duplicate_User{
        override fun httpStatusCode(): Int{
            return 400
        }
    }
    ,
    /**
     * Returned when attempting to access a URL under /cloudservice with an invalid UUID
     */
    Invalid_Cloud_Service_UUID{
        override fun httpStatusCode(): Int{
            return 500
        }
    };

    /**
     * Must be implemented for each enum and should return the HTTP error code as an integer.
     */
    abstract fun httpStatusCode(): Int
    /**
     * Generates the message associated with the Exception using the locale specified.
     *
     * The message must be part of the messages bundle.  Message keys should be in the format common.message.rest.exception. followed by the name of the enum value in lowercase and will all underscores converted to periods.
     * For example, the message for [Duplicate_User] is common.message.rest.exception.duplicate.user
     *
     * @return A user friendly message associated with the exception.
     */
    fun friendlyMessage(locale: Locale) : String {
        return ResourceBundle.getBundle("messages",locale).getString("common.message.rest.exception.${this.name.toLowerCase().replace('_','.')}")
    }
}