import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import App from "./App";
import { DirectoryProvider } from "./context/DirectoryContext";
import { FileProvider } from "./context/FileContext";

const rootElement = document.getElementById("root") as HTMLElement;

if (rootElement) {
  createRoot(rootElement).render(
    <StrictMode>
      <DirectoryProvider>
        <FileProvider>
          <App />
        </FileProvider>
      </DirectoryProvider>
    </StrictMode>
  );
}
