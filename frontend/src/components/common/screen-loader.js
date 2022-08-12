import { ProgressSpinner } from "primereact/progressspinner";

const ScreenLoader = () => {
  return (
    <div className="layout-mask-circle">
      <div className="loader-screen-circle">
        <ProgressSpinner
          style={{ width: "150px", height: "150px" }}
          strokeWidth="3"
          animationDuration="1.5s"
        />
      </div>
    </div>
  );
};

export default ScreenLoader;
