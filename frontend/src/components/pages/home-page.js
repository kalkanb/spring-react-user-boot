import { useSelector } from "react-redux";
import LogoutButton from "../common/button/logout-button";
import AuthenticatedPageContainer from "./authenticated-page-container";

const HomePage = () => {
  const user = useSelector((state) => state.userReducer.user);

  return (
    <AuthenticatedPageContainer>
      <div className="grid p-fluid">
        <div className="field top-button-container top-card clearfix">
          <LogoutButton />
          {user && (
            <div
              style={{
                marginTop: "0.5em",
                textAlign: "left",
                fontWeight: "bold",
              }}
            >
              {user.username}
            </div>
          )}
        </div>
        <div className="card clearfix">{(user && user.username) || ""}</div>
      </div>
    </AuthenticatedPageContainer>
  );
};

export default HomePage;
