import React from "react";
import Footer from "../components/Footer";

function PublicLayout(props) {
  const { children } = props;
  return (
    <React.Fragment>
      <main className="m-3 pt-5">{children}</main>

      <Footer />
    </React.Fragment>
  );
}

export default PublicLayout;
