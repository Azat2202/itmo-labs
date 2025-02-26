import { useAuth } from "react-oidc-context"
import React, {ReactNode} from "react";
import {Link} from "react-router-dom";

function PrivateRoute({ children } : {children: ReactNode}) {
    const auth = useAuth()
    if (auth.isLoading) {
        return (
            <div>
                <h1>Keycloak is loading</h1>
                <h1 >or running authorization code flow with PKCE</h1>
            </div>
        )
    }

    if (auth.error) {
        return (
            <div>
                <h1>Oops ...</h1>
                <h1>{auth.error.message}</h1>
                <Link to="/" onClick={() => {
                    auth.removeUser();
                }}>Go to main page</Link>
            </div>
        )
    }

    if (!auth.isAuthenticated) {
        auth.signinRedirect()
        return null
    }

    return <>
        {children}
    </>
}

export default PrivateRoute