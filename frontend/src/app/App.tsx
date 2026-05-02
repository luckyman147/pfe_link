import { AuthProvider } from "@/features/auth";
import "@/i18n/config";
import { MainLayout } from "@/shared/components/layout/MainLayout";
import { AppRoutes } from "@/routes";

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



