/** @jsxImportSource @emotion/react */
import styled from '@emotion/styled'
import React, { useState } from "react"

const Overlay = styled.div<{ isOpen: boolean }>`
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.3);
    backdrop-filter: blur(5px);
    transition: opacity 0.3s ease-in-out;
    opacity: ${(props) => (props.isOpen ? 1 : 0)};
    pointer-events: ${(props) => (props.isOpen ? 'auto' : 'none')};
    z-index: 1;
`

const SlidingPanelPageContainer = styled.div<{ isOpen: boolean, isSecondPanelOpen: boolean }>`
    position: fixed;
    top: 0;
    right: 0;
    width: calc(100% - 1020px);
    height: 100%;
    background-color: #36acb6;
    transform: translateX(${(props) => (props.isOpen ? (props.isSecondPanelOpen ? '-80px' : '0') : '100%')});
    transition: transform 0.4s ease-in-out;
    box-shadow: -2px 0 10px rgba(0, 0, 0, 0.3);
    z-index: 2;
    display: flex;
    flex-direction: column;
`

const SlidingPanel = styled.div<{ isOpen: boolean }>`
    position: fixed;
    top: 0;
    right: 0;
    width: calc(100% - 1020px);
    height: 100%;
    background-color: #36acb6;
    transform: translateX(${(props) => (props.isOpen ? '0' : '100%')});
    transition: transform 0.4s ease-in-out;
    box-shadow: -2px 0 10px rgba(0, 0, 0, 0.3);
    z-index: 3;
    display: flex;
    flex-direction: column;
`

const SlidingPanelPageHeaderContainer = styled.div`
    color: white;
    margin-top: 20px;
`

const SlidingPanelPageTabsContainer = styled.div`
    display: flex;
    background-color: #f4f4f4;
    width: 320px;
`

const TabButton = styled.button<{ isActive: boolean }>`
    background-color: ${(props) => (props.isActive ? '#128791' : 'transparent')};
    color: ${(props) => (props.isActive ? 'white' : '#333333')};
    width: 160px;
    padding: 10px 20px;
    font-size: 16px;
    cursor: pointer;
    border: none;
    text-align: center;

    &:hover {
        background-color: #07656e;
        color: white;
    }
`

const SlidingPanelPageContentContainer = styled.div`
    background-color: white;
    color: #128791;
    display: flex;
    flex-direction: column;
    flex-grow: 1;
    padding: 20px;
    overflow-y: auto;
`

const Header = styled.h3``

const CloseButton = styled.button`
    position: absolute;
    background-color: transparent;
    color: white;
    border: none;
    font-size: 24px;
    cursor: pointer;

    &:hover {
        color: #ff6b6b;
    }
`

const OpenSecondPanelButton = styled.button`
    margin-top: 20px;
    padding: 10px;
    background-color: #128791;
    color: white;
    border: none;
    cursor: pointer;

    &:hover {
        background-color: #07656e;
    }
`

type Props = {
  isOpen: boolean
  onClose: () => void
}

const SlidingPanelPage: React.FC<Props> = ({ isOpen, onClose }: Props) => {
  const [activeTab, setActiveTab] = useState(0);
  const [isSecondPanelOpen, setSecondPanelOpen] = useState(false);

  const handleTabChange = (index: number) => {
    setActiveTab(index);
  };

  const openSecondPanel = () => {
    setSecondPanelOpen(true);
  };

  const closeSecondPanel = () => {
    setSecondPanelOpen(false);
  };

  return (
    <div>
      <Overlay isOpen={isOpen} />
      <SlidingPanelPageContainer isOpen={isOpen} isSecondPanelOpen={isSecondPanelOpen}>
        <CloseButton onClick={onClose}>×</CloseButton>
        <SlidingPanelPageHeaderContainer>
          <Header>Sliding Panel</Header>
        </SlidingPanelPageHeaderContainer>
        <SlidingPanelPageTabsContainer>
          <TabButton isActive={activeTab === 0} onClick={() => handleTabChange(0)}>
            Tab 1
          </TabButton>
          <TabButton isActive={activeTab === 1} onClick={() => handleTabChange(1)}>
            Tab 2
          </TabButton>
        </SlidingPanelPageTabsContainer>
        <SlidingPanelPageContentContainer>
          {activeTab === 0 ? (
            <div>Content for Tab 1</div>
          ) : (
            <div>Content for Tab 2</div>
          )}
          <OpenSecondPanelButton onClick={openSecondPanel}>Open Additional Panel</OpenSecondPanelButton>
        </SlidingPanelPageContentContainer>
      </SlidingPanelPageContainer>

      <SlidingPanel isOpen={isSecondPanelOpen}>
        <CloseButton onClick={closeSecondPanel}>×</CloseButton>
        <SlidingPanelPageHeaderContainer>
          <Header>Additional Panel</Header>
        </SlidingPanelPageHeaderContainer>

        <SlidingPanelPageContentContainer>Content for the second panel</SlidingPanelPageContentContainer>
      </SlidingPanel>
    </div>
  );
};

export default SlidingPanelPage;
