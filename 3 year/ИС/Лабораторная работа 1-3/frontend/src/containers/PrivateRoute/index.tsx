import { ReactNode } from "react";
import { useSelector } from "react-redux";
import { RootState } from "../../store/store";
import { Navigate } from "react-router-dom";

function PrivateRoute({ children } : {children: ReactNode}) {
  const token = useSelector((state: RootState) => state.auth.token);
  if (!token) {
    return <Navigate to="/" />;
  }

  return <>{children}</>;
}

export default PrivateRoute