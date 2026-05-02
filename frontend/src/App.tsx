import "./i18n/config";
import { MainLayout } from "./components/layout/MainLayout";
import { AppRoutes } from "./routes";
import { AuthProvider } from "./features/auth";

function App() {
  return (
    <AuthProvider>
      <MainLayout>
        <AppRoutes />
      </MainLayout>
    </AuthProvider>
  );
}


export default App;



