import SlidingPanelPage from "./pages/SlidingPanelPage.tsx"
import styled from "@emotion/styled"
import { useState } from "react"

const SlideInButton = styled.button`
    position: absolute;
    top: 20px;
    left: 20px;
    z-index: 1001;
    background-color: #ff5733;
    color: white;
    border: none;
    padding: 10px 20px;
    cursor: pointer;
`

const SlidingPanel = () => {

  const [isPanelOpen, setIsPanelOpen] = useState(false)

  const togglePanel = () => {
    setIsPanelOpen(!isPanelOpen)
  }

  const onClose = () => {
    setIsPanelOpen(false)
  }

  return (
    <>
      <SlideInButton onClick={togglePanel}>
        {isPanelOpen ? 'Close Panel' : 'Open Panel'}
      </SlideInButton>

      <SlidingPanelPage isOpen={isPanelOpen} onClose={onClose}/>
    </>
  )
}

export default SlidingPanel
